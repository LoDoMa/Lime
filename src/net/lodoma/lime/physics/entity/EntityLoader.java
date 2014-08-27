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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class EntityLoader
{
    public static final String ENTITY_DESCRIPTION_FILE_EXTENSION = ".xml";
    private static int idCounter = 0;
    
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
        return newEntity(entityWorld, physicsWorld, propertyPool, hash, idCounter++);
    }
    
    public Entity newEntity(EntityWorld entityWorld, PhysicsWorld physicsWorld, PropertyPool propertyPool, int hash, int id) throws EntityLoaderException
    {
        try
        {
            EntityData data = entityData.get(hash);
            return new Entity(entityWorld, physicsWorld, propertyPool, data, id);
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
                Element body = XMLHelper.nodeToElement(bodies.item(i));
                Pair<Integer, PhysicsBodyData> bodyData = parseBodyElement(body);
                data.bodies.put(bodyData.first, new PhysicsBodyDescription(bodyData.second));
            }
            
            for(int i = 0; i < revoluteJoints.getLength(); i++)
            {
                Element joint = XMLHelper.nodeToElement(revoluteJoints.item(i));
                Pair<Integer, PhysicsJointData> jointData = parseRevoluteJointElement(joint);
                data.joints.put(jointData.first, new PhysicsJointDescription(jointData.second));
            }
            
            for(int i = 0; i < masks.getLength(); i++)
            {
                Element mask = XMLHelper.nodeToElement(masks.item(i));
                Pair<Integer, Mask> maskData = parseMaskElement(mask);
                data.masks.put(maskData.first, maskData.second);
            }
            
            return data;
        }
        catch(IOException | SAXException | ParserConfigurationException | RuntimeException e)
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
        
        Element shape = XMLHelper.getUniqueElement(bodyElement, "shape", new String[] { "polygon_shape", "circle_shape" });
        
        switch(shape.getNodeName())
        {
        case "polygon_shape":
            PolygonShape polygonShape = parsePolygonShapeElement(shape);
            
            Vector2[] vertices = new Vector2[polygonShape.vertexCount];
            for(int i = 0; i < polygonShape.vertexCount; i++)
                vertices[i] = new Vector2(polygonShape.vertices[i].x, polygonShape.vertices[i].y);
            
            data.shapeType = ShapeType.POLYGON;
            data.shapeVertices = vertices;
            
            break;
        case "circle_shape":
            CircleShape circleShape = parseCircleShapeElement(shape);
            
            data.shapeType = ShapeType.CIRCLE;
            data.shapeRadius = circleShape.radius;
            break;
        }

        Element behavior = XMLHelper.getUniqueElement(bodyElement, "behavior",
                new String[] { "static_behavior", "dynamic_behavior", "kinematic_behavior" });
        
        switch(behavior.getNodeName())
        {
        case "static_behavior":
            data.bodyType = PhysicsBodyType.STATIC;
            break;
        case "dynamic_behavior":
            DynamicBehavior dynamicBehavior = parseDynamicBehaviorElement(behavior);
            
            data.bodyType = PhysicsBodyType.DYNAMIC;
            data.density = dynamicBehavior.density;
            data.friction = dynamicBehavior.friction;
            data.restitution = dynamicBehavior.restitution;
            break;
        case "kinematic_behavior":
            data.bodyType = PhysicsBodyType.KINEMATIC;
            break;
        }
        
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

        Element anchorA = XMLHelper.getUniqueElement(revoluteJointElement, "anchor_a");
        Element anchorB = XMLHelper.getUniqueElement(revoluteJointElement, "anchor_b");
        
        joint.anchorA = parseVectorElement(anchorA);
        joint.anchorB = parseVectorElement(anchorB);
        
        return new Pair<Integer, PhysicsJointData>(hash, joint);
    }
    
    private Pair<Integer, Mask> parseColoredLayerElement(Element coloredLayerElement) throws EntityLoaderException
    {
        int layerHeight = XMLHelper.getDeepValueInteger(coloredLayerElement, "layer_height");

        Element shape = XMLHelper.getUniqueElement(coloredLayerElement, "shape", new String[] { "polygon_shape", "circle_shape" });
        
        ColoredMask mask = null;
        
        switch(shape.getNodeName())
        {
        case "polygon_shape":
            PolygonShape polygonShape = parsePolygonShapeElement(shape);
            
            int n = polygonShape.vertexCount;
            float[][] arrays = new float[6][n];
            for(int i = 0; i < n; i++)
            {
                Vertex vertex = polygonShape.vertices[i];
                arrays[0][i] = vertex.x;
                arrays[1][i] = vertex.y;
                for(int j = 0; j < 4; j++)
                    arrays[j + 2][i] = ((vertex.color >>> (24 - i * 8)) & 0xFF) / (float) (0xFF);
            }
            
            mask = new ColoredMask(n, arrays[0], arrays[1], arrays[2], arrays[3], arrays[4], arrays[5]);
            
            break;
        case "circle_shape":
            CircleShape circleShape = parseCircleShapeElement(shape);
            
            float[] color = new float[4];
            for(int i = 0; i < 4; i++)
                color[i] = ((circleShape.color >>> (24 - i * 8)) & 0xFF) / (float) 0xFF;
            mask = new ColoredMask(circleShape.radius, color[0], color[1], color[2], color[3]);
            break;
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
            Element coloredLayer = XMLHelper.nodeToElement(coloredLayers.item(i));
            Pair<Integer, Mask> layerData = parseColoredLayerElement(coloredLayer);
            if(layers.containsKey(layerData.first))
                throw new EntityLoaderException("duplicate layer height");
            layers.put(layerData.first, layerData.second);
        }
        
        Set<Integer> heights = layers.keySet();
        for(Integer height : heights)
            mask.addLayer(layers.get(height));
        
        return new Pair<Integer, Mask>(hash, mask);
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
    
    private PolygonShape parsePolygonShapeElement(Element polygonShapeElement) throws EntityLoaderException
    {
        PolygonShape polygonShape = new PolygonShape();
        
        int vertexCount = XMLHelper.getDeepValueInteger(polygonShapeElement, "vertex_count");
        polygonShape.vertexCount = vertexCount;
        polygonShape.vertices = new Vertex[vertexCount];
        
        NodeList vertices = polygonShapeElement.getElementsByTagName("vertex");
        for(int i = 0; i < vertices.getLength(); i++)
        {
            Element vertexElement = XMLHelper.nodeToElement(vertices.item(i));
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
    
    private Vector2 parseVectorElement(Element vectorElement)
    {
        float x = XMLHelper.getDeepValueFloat(vectorElement, "x");
        float y = XMLHelper.getDeepValueFloat(vectorElement, "y");
        return new Vector2(x, y);
    }
    
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
}
