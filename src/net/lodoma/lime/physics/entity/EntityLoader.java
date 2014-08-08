package net.lodoma.lime.physics.entity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.lodoma.lime.common.PropertyPool;
import net.lodoma.lime.mask.ColoredMask;
import net.lodoma.lime.mask.LayeredMask;
import net.lodoma.lime.mask.Mask;
import net.lodoma.lime.mask.RenderingOrder;
import net.lodoma.lime.physics.PhysicsBodyData;
import net.lodoma.lime.physics.PhysicsBodyDescription;
import net.lodoma.lime.physics.PhysicsBodyType;
import net.lodoma.lime.physics.PhysicsJointData;
import net.lodoma.lime.physics.PhysicsJointDescription;
import net.lodoma.lime.physics.PhysicsJointType;
import net.lodoma.lime.physics.PhysicsWorld;
import net.lodoma.lime.physics.ShapeType;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.util.Pair;
import net.lodoma.lime.util.Vector2;
import net.lodoma.lime.util.XMLHelper;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class EntityLoader
{
    public static final String ENTITY_DESCRIPTION_FILE_EXTENSION = ".xml";
    
    private class Vertex
    {
        public int index;
        public float x;
        public float y;
        public int color;
    }
    
    private class PolygonShape
    {
        public int vertexCount;
        public Vertex[] vertices;
    }
    
    private class CircleShape
    {
        public float radius;
        public int color;
    }
    
    private class DynamicBehavior
    {
        public float density;
        public float friction;
        public float restitution;
    }
    
    private Map<Integer, EntityData> entityData;
    
    public EntityLoader()
    {
        entityData = new HashMap<Integer, EntityData>();
    }
    
    public void addAllFiles(EntityWorld world, PropertyPool propertyPool) throws EntityLoaderException
    {
        List<File> entityFiles = getEntityFileList(new File("model"));
        for(File entityFile : entityFiles)
        {
            EntityData entity = loadDataFromXML(entityFile);
            entityData.put(entity.nameHash, entity);
        }
    }
    
    public Entity newEntity(EntityWorld entityWorld, PhysicsWorld physicsWorld, PropertyPool propertyPool, int hash) throws EntityLoaderException
    {
        try
        {
            EntityData data = entityData.get(hash);
            return new Entity(entityWorld, physicsWorld, propertyPool, data);
        }
        catch(IOException e)
        {
            throw new EntityLoaderException(e);
        }
    }
    
    private List<File> getEntityFileList(File directory)
    {
        List<File> fileList = new ArrayList<File>();
        File[] files = directory.listFiles();
        for(File file : files)
        {
            if(file.isDirectory())
                fileList.addAll(getEntityFileList(file));
            else
                if(file.getName().toLowerCase().endsWith(ENTITY_DESCRIPTION_FILE_EXTENSION))
                    fileList.add(file);
                
        }
        return fileList;
    }
    
    private EntityData loadDataFromXML(File xmlFile) throws EntityLoaderException
    {
        try
        {
            EntityData data = new EntityData();
            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            Element docElement = doc.getDocumentElement();
            docElement.normalize();
    
            
            String rootName = docElement.getNodeName();
            if(rootName != "model")
                throw new EntityLoaderException("root of an entity XML file must be named \"model\"");
            
            data.name         = XMLHelper.getDeepValue(docElement, "model_name");
            data.version      = XMLHelper.getDeepValue(docElement, "model_version");
            data.nameHash     = HashHelper.hash32(data.name);
            
            NodeList bodies         = docElement.getElementsByTagName("body");
            NodeList revoluteJoints = docElement.getElementsByTagName("revolute_joint");
            NodeList masks          = docElement.getElementsByTagName("mask");
            data.script             = XMLHelper.getDeepValue(docElement, "script");
            
            for(int i = 0; i < bodies.getLength(); i++)
            {
                Node bodyNode = bodies.item(i);
                if(bodyNode.getNodeType() != Node.ELEMENT_NODE)
                    throw new EntityLoaderException("invalid \"body\" node");
                
                Pair<Integer, PhysicsBodyData> bodyData = parseBodyElement((Element) bodyNode);
                data.bodies.put(bodyData.first, new PhysicsBodyDescription(bodyData.second));
            }
            
            for(int i = 0; i < revoluteJoints.getLength(); i++)
            {
                Node jointNode = revoluteJoints.item(i);
                if(jointNode.getNodeType() != Node.ELEMENT_NODE)
                    throw new EntityLoaderException("invalid \"revolute_joint\" node");
                
                Pair<Integer, PhysicsJointData> jointData = parseRevoluteJointElement((Element) jointNode);
                data.joints.put(jointData.first, new PhysicsJointDescription(jointData.second));
            }
            
            for(int i = 0; i < masks.getLength(); i++)
            {
                Node maskNode = masks.item(i);
                if(maskNode.getNodeType() != Node.ELEMENT_NODE)
                    throw new EntityLoaderException("invalid \"mask\" node");
                
                Pair<Integer, Mask> maskData = parseMaskElement((Element) maskNode);
                data.masks.put(maskData.first, maskData.second);
            }
            
            return data;
        }
        catch(IOException | SAXException | ParserConfigurationException e)
        {
            throw new EntityLoaderException(e);
        }
    }
    
    private Pair<Integer, PhysicsBodyData> parseBodyElement(Element bodyElement) throws EntityLoaderException
    {
        PhysicsBodyData data = new PhysicsBodyData();
        data.position = new Vector2(0.0f, 0.0f);
        data.angle = 0.0f;
        
        int hash = HashHelper.hash32(XMLHelper.getDeepValue(bodyElement, "name"));
        
        NodeList polygonShapes = bodyElement.getElementsByTagName("polygon_shape");
        NodeList circleShapes  = bodyElement.getElementsByTagName("circle_shape");
        int shapeTotal = polygonShapes.getLength() + circleShapes.getLength();
        
        if(shapeTotal == 0) throw new EntityLoaderException("missing shape in \"body\"");
        if(shapeTotal > 1) throw new EntityLoaderException("multiple shapes not allowed in \"body\"");
        
        if(polygonShapes.getLength() == 1)
        {
            Node polygonShapeNode = polygonShapes.item(0);
            if(polygonShapeNode.getNodeType() != Node.ELEMENT_NODE)
                throw new EntityLoaderException("invalid \"polygon_shape\" node");
            Element polygonShapeElement = (Element) polygonShapeNode;
            PolygonShape polygonShape = parsePolygonShapeElement(polygonShapeElement);
            
            Vector2[] vertices = new Vector2[polygonShape.vertexCount];
            for(int i = 0; i < polygonShape.vertexCount; i++)
                vertices[i] = new Vector2(polygonShape.vertices[i].x, polygonShape.vertices[i].y);
            
            data.shapeType = ShapeType.POLYGON;
            data.shapeVertices = vertices;
        }
        else if(circleShapes.getLength() == 1)
        {
            Node circleShapeNode = circleShapes.item(0);
            if(circleShapeNode.getNodeType() != Node.ELEMENT_NODE)
                throw new EntityLoaderException("invalid \"circle_shape\" node");
            Element circleShapeElement = (Element) circleShapeNode;
            CircleShape circleShape = parseCircleShapeElement(circleShapeElement);
            
            data.shapeType = ShapeType.CIRCLE;
            data.shapeRadius = circleShape.radius;
        }

        NodeList staticBehaviors  = bodyElement.getElementsByTagName("static_behavior");
        NodeList dynamicBehaviors = bodyElement.getElementsByTagName("dynamic_behavior");
        NodeList kinematicBehaviors = bodyElement.getElementsByTagName("kinematic_behavior");
        int behaviorTotal = staticBehaviors.getLength() + dynamicBehaviors.getLength() + kinematicBehaviors.getLength();

        if(behaviorTotal == 0) throw new EntityLoaderException("missing behavior in \"body\"");
        if(behaviorTotal > 1) throw new EntityLoaderException("multiple behaviors not allowed in \"body\"");
        
        if(staticBehaviors.getLength() == 1)
            data.bodyType = PhysicsBodyType.STATIC;
        else if(dynamicBehaviors.getLength() == 1)
        {
            Node dynamicBehaviorNode = dynamicBehaviors.item(0);
            if(dynamicBehaviorNode.getNodeType() != Node.ELEMENT_NODE)
                throw new EntityLoaderException("invalid \"dynamic_behavior\" node");
            Element dynamicBehaviorElement = (Element) dynamicBehaviorNode;
            DynamicBehavior dynamicBehavior = parseDynamicBehaviorElement(dynamicBehaviorElement);
            
            data.bodyType = PhysicsBodyType.DYNAMIC;
            data.density = dynamicBehavior.density;
            data.friction = dynamicBehavior.friction;
            data.restitution = dynamicBehavior.restitution;
        }
        else if(kinematicBehaviors.getLength() == 1)
            data.bodyType = PhysicsBodyType.KINEMATIC;
        
        return new Pair<Integer, PhysicsBodyData>(hash, data);
    }
    
    private Pair<Integer, PhysicsJointData> parseRevoluteJointElement(Element revoluteJointElement) throws EntityLoaderException
    {
        PhysicsJointData joint = new PhysicsJointData();
        joint.jointType = PhysicsJointType.REVOLUTE;
        
        int hash = HashHelper.hash32(XMLHelper.getDeepValue(revoluteJointElement, "name"));

        joint.bodyA = HashHelper.hash32(XMLHelper.getDeepValue(revoluteJointElement, "body_a"));
        joint.bodyB = HashHelper.hash32(XMLHelper.getDeepValue(revoluteJointElement, "body_b"));
        joint.collide = XMLHelper.getDeepValueBoolean(revoluteJointElement, "collision");
        
        joint.angle = XMLHelper.getDeepValueFloat(revoluteJointElement, "rotation");

        Node anchorANode = XMLHelper.getUniqueNode(revoluteJointElement, "anchor_a");
        Node anchorBNode = XMLHelper.getUniqueNode(revoluteJointElement, "anchor_b");
        
        if(anchorANode.getNodeType() != Node.ELEMENT_NODE)
            throw new EntityLoaderException("invalid \"anchor_a\" node");
        Element anchorAElement = (Element) anchorANode;
        joint.anchorA = parseVectorElement(anchorAElement);
        
        if(anchorBNode.getNodeType() != Node.ELEMENT_NODE)
            throw new EntityLoaderException("invalid \"anchor_b\" node");
        Element anchorBElement = (Element) anchorBNode;
        joint.anchorB = parseVectorElement(anchorBElement);
        
        return new Pair<Integer, PhysicsJointData>(hash, joint);
    }
    
    private Pair<Integer, Mask> parseColoredLayerElement(Element coloredLayerElement) throws EntityLoaderException
    {
        int layerHeight = XMLHelper.getDeepValueInteger(coloredLayerElement, "layer_height");
        
        NodeList polygonShapes = coloredLayerElement.getElementsByTagName("polygon_shape");
        NodeList circleShapes = coloredLayerElement.getElementsByTagName("circle_shape");
        int shapeTotal = polygonShapes.getLength() + circleShapes.getLength();
        
        if(shapeTotal < 1)
            throw new EntityLoaderException("missing shape in \"colored_layer\"");
        if(shapeTotal > 1)
            throw new EntityLoaderException("multiple shapes nodes not allowed in \"colored_layer\"");
        
        ColoredMask mask = null;
        
        if(polygonShapes.getLength() > 0)
        {
            Node polygonShapeNode = polygonShapes.item(0);
            if(polygonShapeNode.getNodeType() != Node.ELEMENT_NODE)
                throw new EntityLoaderException("invalid \"polygon_shape\" node");
            Element polygonShapeElement = (Element) polygonShapeNode;
            PolygonShape polygonShape = parsePolygonShapeElement(polygonShapeElement);
            
            int n = polygonShape.vertexCount;
            float[] x = new float[n];
            float[] y = new float[n];
            float[] r = new float[n];
            float[] g = new float[n];
            float[] b = new float[n];
            float[] a = new float[n];
            for(int i = 0; i < n; i++)
            {
                Vertex vertex = polygonShape.vertices[i];
                x[i] = vertex.x;
                y[i] = vertex.y;
                r[i] = ((vertex.color >>> 24) & 0xFF) / (float) (0xFF);
                g[i] = ((vertex.color >>> 16) & 0xFF) / (float) (0xFF);
                b[i] = ((vertex.color >>> 8 ) & 0xFF) / (float) (0xFF);
                a[i] = ((vertex.color >>> 0 ) & 0xFF) / (float) (0xFF);
            }
            
            mask = new ColoredMask(n, x, y, r, g, b, a);
        }
        else if(circleShapes.getLength() > 0)
        {
            Node circleShapeNode = circleShapes.item(0);
            if(circleShapeNode.getNodeType() != Node.ELEMENT_NODE)
                throw new EntityLoaderException("invalid \"circle_shape\" node");
            Element circleShapeElement = (Element) circleShapeNode;
            CircleShape circleShape = parseCircleShapeElement(circleShapeElement);

            float r = ((circleShape.color >>> 24) & 0xFF) / (float) 0xFF;
            float g = ((circleShape.color >>> 16) & 0xFF) / (float) 0xFF;
            float b = ((circleShape.color >>> 8 ) & 0xFF) / (float) 0xFF;
            float a = ((circleShape.color >>> 0 ) & 0xFF) / (float) 0xFF;
            mask = new ColoredMask(circleShape.radius, r, g, b, a);
        }
        
        return new Pair<Integer, Mask>(layerHeight, mask);
    }
    
    private Pair<Integer, Mask> parseMaskElement(Element maskElement) throws EntityLoaderException
    {
        LayeredMask mask = new LayeredMask(RenderingOrder.BOTTOM_TO_TOP);
        
        int hash = HashHelper.hash32(XMLHelper.getDeepValue(maskElement, "name"));
        
        Map<Integer, Mask> layers = new HashMap<Integer, Mask>();
        
        NodeList coloredLayers = maskElement.getElementsByTagName("colored_layer");
        
        for(int i = 0; i < coloredLayers.getLength(); i++)
        {
            Node coloredLayerNode = coloredLayers.item(i);
            if(coloredLayerNode.getNodeType() != Node.ELEMENT_NODE)
                throw new EntityLoaderException("invalid \"colored_layer\" node");
            Element coloredLayerElement = (Element) coloredLayerNode;
            Pair<Integer, Mask> layerData = parseColoredLayerElement(coloredLayerElement);
            if(layers.containsKey(layerData.first))
                throw new EntityLoaderException("duplicate layer height");
            layers.put(layerData.first, layerData.second);
        }
        
        Set<Integer> heights = layers.keySet();
        for(Integer height : heights)
            mask.addLayer(layers.get(height));
        
        return new Pair<Integer, Mask>(hash, mask);
    }
    
    private Vertex parseVertexElement(Element vertexElement)
    {
        Vertex vertex = new Vertex();
        
        if(XMLHelper.hasNode(vertexElement, "index"))
            vertex.index = XMLHelper.getDeepValueInteger(vertexElement, "index");
        vertex.x = XMLHelper.getDeepValueFloat(vertexElement, "x");
        vertex.y = XMLHelper.getDeepValueFloat(vertexElement, "y");
        if(XMLHelper.hasNode(vertexElement, "color"))
            vertex.color = XMLHelper.getDeepValueInteger(vertexElement, "color");
        
        return vertex;
    }
    
    private PolygonShape parsePolygonShapeElement(Element polygonShapeElement) throws EntityLoaderException
    {
        PolygonShape polygonShape = new PolygonShape();
        
        int vertexCount = XMLHelper.getDeepValueInteger(polygonShapeElement, "vertex_count");
        polygonShape.vertexCount = vertexCount;
        polygonShape.vertices = new Vertex[vertexCount];
        
        NodeList vertices = polygonShapeElement.getElementsByTagName("vertex");
        for(int i = 0; i < vertices.getLength(); i++)
        {
            Node vertexNode = vertices.item(i);
            if(vertexNode.getNodeType() != Node.ELEMENT_NODE)
                throw new EntityLoaderException("invalid \"vertex\" node");
            Element vertexElement = (Element) vertexNode;
            Vertex vertex = parseVertexElement(vertexElement);
            polygonShape.vertices[vertex.index - 1] = vertex;
        }
        
        return polygonShape;
    }
    
    private CircleShape parseCircleShapeElement(Element circleShapeElement)
    {
        CircleShape circleShape = new CircleShape();
        
        float radius = XMLHelper.getDeepValueFloat(circleShapeElement, "radius");
        circleShape.radius = radius;
        if(XMLHelper.hasNode(circleShapeElement, "color"))
            circleShape.color = XMLHelper.getDeepValueInteger(circleShapeElement, "color");
        
        return circleShape;
    }
    
    private DynamicBehavior parseDynamicBehaviorElement(Element dynamicBehaviorElement)
    {
        DynamicBehavior dynamicBehavior = new DynamicBehavior();

        float density     = XMLHelper.getDeepValueFloat(dynamicBehaviorElement, "density");
        float friction    = XMLHelper.getDeepValueFloat(dynamicBehaviorElement, "friction");
        float restitution = XMLHelper.getDeepValueFloat(dynamicBehaviorElement, "restitution");
        dynamicBehavior.density     = density;
        dynamicBehavior.friction    = friction;
        dynamicBehavior.restitution = restitution;
        
        return dynamicBehavior;
    }
    
    private Vector2 parseVectorElement(Element vectorElement)
    {
        float x = XMLHelper.getDeepValueFloat(vectorElement, "x");
        float y = XMLHelper.getDeepValueFloat(vectorElement, "y");
        return new Vector2(x, y);
    }
}
