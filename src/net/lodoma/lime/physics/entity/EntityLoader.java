package net.lodoma.lime.physics.entity;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.naming.InvalidNameException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.lodoma.lime.event.EventManager;
import net.lodoma.lime.mask.ColoredMask;
import net.lodoma.lime.mask.LayeredMask;
import net.lodoma.lime.mask.Mask;
import net.lodoma.lime.mask.RenderingOrder;
import net.lodoma.lime.physics.PhysicsBody;
import net.lodoma.lime.physics.PhysicsBodyType;
import net.lodoma.lime.physics.PhysicsJoint;
import net.lodoma.lime.physics.PhysicsJointType;
import net.lodoma.lime.script.LuaScript;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.util.HashPool32;
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
    }
    
    private class StaticBehavior
    {
        
    }
    
    private class DynamicBehavior
    {
        public float density;
        public float friction;
        public float restitution;
    }
    
    private class KinematicBehavior
    {
        
    }
    
    private Map<Long, File> files;
    
    public EntityLoader()
    {
        files = new HashMap<Long, File>();
    }
    
    public void addXMLFile(String internalName, File file)
    {
        files.put(HashHelper.hash64(internalName), file);
    }
    
    public File getXMLFileByHash(long hash)
    {
        return files.get(hash);
    }
    
    public Entity loadFromXML(File xmlFile, EntityWorld world, HashPool32<EventManager> emanPool)
            throws IOException, SAXException, ParserConfigurationException
    {
        Entity entity = new Entity(emanPool);
        
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        Element docElement = doc.getDocumentElement();
        docElement.normalize();
        
        String rootName = docElement.getNodeName();
        if(rootName != "model")
            new InvalidNameException("root of an entity XML file must be named \"model\"");
        
        String name       = XMLHelper.getDeepValue(docElement, "model_name");
        String visualName = XMLHelper.getDeepValue(docElement, "model_visual");
        String version    = XMLHelper.getDeepValue(docElement, "model_version");
        
        entity.internalName = name;
        entity.visualName = visualName;
        entity.version = version;
        
        entity.hash = HashHelper.hash64(entity.internalName);
        entity.world = world;
        
        NodeList bodies         = docElement.getElementsByTagName("body");
        NodeList revoluteJoints = docElement.getElementsByTagName("revolute_joint");
        NodeList masks          = docElement.getElementsByTagName("mask");
        NodeList properties     = docElement.getElementsByTagName("property");

        for(int i = 0; i < bodies.getLength(); i++)
        {
            Node bodyNode = bodies.item(i);
            if(bodyNode.getNodeType() != Node.ELEMENT_NODE)
                throw new RuntimeException("invalid \"body\" node");
            Pair<String, PhysicsBody> bodyData = parseBodyElement((Element) bodyNode);
            entity.bodies.put(HashHelper.hash32(bodyData.first), bodyData.second);
        }
        for(int i = 0; i < revoluteJoints.getLength(); i++)
        {
            Node jointNode = revoluteJoints.item(i);
            if(jointNode.getNodeType() != Node.ELEMENT_NODE)
                throw new RuntimeException("invalid \"revolute_joint\" node");
            Pair<String, PhysicsJoint> jointData = parseRevoluteJointElement((Element) jointNode, entity);
            entity.joints.put(HashHelper.hash32(jointData.first), jointData.second);
        }
        for(int i = 0; i < masks.getLength(); i++)
        {
            Node maskNode = masks.item(i);
            if(maskNode.getNodeType() != Node.ELEMENT_NODE)
                throw new RuntimeException("invalid \"mask\" node");
            Pair<String, Mask> maskData = parseMaskElement((Element) maskNode, entity);
            entity.masks.put(HashHelper.hash32(maskData.first), maskData.second);
        }
        for(int i = 0; i < properties.getLength(); i++)
        {
            Node propertyNode = properties.item(i);
            if(propertyNode.getNodeType() != Node.ELEMENT_NODE)
                throw new RuntimeException("invalid \"property\" node");
            Pair<String, String> propertyData = parsePropertyElement((Element) propertyNode);
            entity.properties.put(HashHelper.hash32(propertyData.first), propertyData.second);
        }
        
        entity.script = new LuaScript(new File(XMLHelper.getDeepValue(docElement, "script")), entity);
        
        return entity;
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
    
    private PolygonShape parsePolygonShapeElement(Element polygonShapeElement)
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
                throw new RuntimeException("invalid \"vertex\" node");
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
        
        return circleShape;
    }
    
    @SuppressWarnings("unused")
    private StaticBehavior parseStaticBehaviorElement(Element staticBehaviorElement)
    {
        StaticBehavior staticBehavior = new StaticBehavior();
        
        return staticBehavior;
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
    
    @SuppressWarnings("unused")
    private KinematicBehavior parseKinematicBehaviorElement(Element kinematicBehaviorElement)
    {
        KinematicBehavior kinematicBehavior = new KinematicBehavior();
        
        return kinematicBehavior;
    }
    
    private Vector2 parseVectorElement(Element vectorElement)
    {
        float x = XMLHelper.getDeepValueFloat(vectorElement, "x");
        float y = XMLHelper.getDeepValueFloat(vectorElement, "y");
        return new Vector2(x, y);
    }
    
    private Pair<String, PhysicsBody> parseBodyElement(Element bodyElement)
    {
        PhysicsBody body = new PhysicsBody();
        String name = XMLHelper.getDeepValue(bodyElement, "name");
        
        NodeList polygonShapes = bodyElement.getElementsByTagName("polygon_shape");
        NodeList circleShapes  = bodyElement.getElementsByTagName("circle_shape");
        int shapeTotal = polygonShapes.getLength() + circleShapes.getLength();
        
        if(shapeTotal == 0) throw new RuntimeException("missing shape in \"body\"");
        if(shapeTotal > 1) throw new RuntimeException("multiple shapes not allowed in \"body\"");
        
        if(polygonShapes.getLength() == 1)
        {
            Node polygonShapeNode = polygonShapes.item(0);
            if(polygonShapeNode.getNodeType() != Node.ELEMENT_NODE)
                throw new RuntimeException("invalid \"polygon_shape\" node");
            Element polygonShapeElement = (Element) polygonShapeNode;
            PolygonShape polygonShape = parsePolygonShapeElement(polygonShapeElement);
            
            Vector2[] vertices = new Vector2[polygonShape.vertexCount];
            for(int i = 0; i < polygonShape.vertexCount; i++)
                vertices[i] = new Vector2(polygonShape.vertices[i].x, polygonShape.vertices[i].y);
            
            body.setPolygonShape(vertices);
        }
        else if(circleShapes.getLength() == 1)
        {
            Node circleShapeNode = circleShapes.item(0);
            if(circleShapeNode.getNodeType() != Node.ELEMENT_NODE)
                throw new RuntimeException("invalid \"circle_shape\" node");
            Element circleShapeElement = (Element) circleShapeNode;
            CircleShape circleShape = parseCircleShapeElement(circleShapeElement);
            
            body.setCircleShape(circleShape.radius);
        }

        NodeList staticBehaviors  = bodyElement.getElementsByTagName("static_behavior");
        NodeList dynamicBehaviors = bodyElement.getElementsByTagName("dynamic_behavior");
        NodeList kineticBehaviors = bodyElement.getElementsByTagName("kinetic_behavior");
        int behaviorTotal = staticBehaviors.getLength() + dynamicBehaviors.getLength() + kineticBehaviors.getLength();

        if(behaviorTotal == 0) throw new RuntimeException("missing behavior in \"body\"");
        if(behaviorTotal > 1) throw new RuntimeException("multiple behaviors not allowed in \"body\"");
        
        if(staticBehaviors.getLength() == 1)
        {
            /*
            Node staticBehaviorNode = staticBehaviors.item(0);
            if(staticBehaviorNode.getNodeType() != Node.ELEMENT_NODE)
                throw new RuntimeException("invalid \"static_behavior\" node");
            Element staticBehaviorElement = (Element) staticBehaviorNode;
            StaticBehavior staticBehavior = parseStaticBehaviorElement(staticBehaviorElement);
            */
            
            body.setBodyType(PhysicsBodyType.STATIC);
        }
        else if(dynamicBehaviors.getLength() == 1)
        {
            Node dynamicBehaviorNode = dynamicBehaviors.item(0);
            if(dynamicBehaviorNode.getNodeType() != Node.ELEMENT_NODE)
                throw new RuntimeException("invalid \"dynamic_behavior\" node");
            Element dynamicBehaviorElement = (Element) dynamicBehaviorNode;
            DynamicBehavior dynamicBehavior = parseDynamicBehaviorElement(dynamicBehaviorElement);
            
            body.setBodyType(PhysicsBodyType.DYNAMIC);
            body.setDensity(dynamicBehavior.density);
            body.setFriction(dynamicBehavior.friction);
            body.setRestitution(dynamicBehavior.restitution);
        }
        else if(kineticBehaviors.getLength() == 1)
        {
            /*
            Node kinematicBehaviorNode = kinematicBehaviors.item(0);
            if(kinematicBehaviorNode.getNodeType() != Node.ELEMENT_NODE)
                throw new RuntimeException("invalid \"kinematic_behavior\" node");
            Element kinematicBehaviorElement = (Element) kinematicBehaviorNode;
            KinematicBehavior kinematicBehavior = parseKinematicBehaviorElement(kinematicBehaviorElement);
            */
            
            body.setBodyType(PhysicsBodyType.KINEMATIC);
        }
        
        return new Pair<String, PhysicsBody>(name, body);
    }
    
    private Pair<String, PhysicsJoint> parseRevoluteJointElement(Element revoluteJointElement, Entity entity)
    {
        PhysicsJoint joint = new PhysicsJoint(PhysicsJointType.REVOLUTE);
        String name = XMLHelper.getDeepValue(revoluteJointElement, "name");

        String nameBodyA = XMLHelper.getDeepValue(revoluteJointElement, "body_a");
        String nameBodyB = XMLHelper.getDeepValue(revoluteJointElement, "body_b");
        boolean collision = XMLHelper.getDeepValueBoolean(revoluteJointElement, "collision");
        float rotation = XMLHelper.getDeepValueFloat(revoluteJointElement, "rotation");
        
        NodeList anchorAs = revoluteJointElement.getElementsByTagName("anchor_a");
        NodeList anchorBs = revoluteJointElement.getElementsByTagName("anchor_b");

        if(anchorAs.getLength() < 1)
            throw new RuntimeException("missing \"anchor_a\" node in \"revolute_joint\"");
        if(anchorAs.getLength() > 1)
            throw new RuntimeException("multiple \"anchor_a\" nodes not allowed in \"revolute_joint\"");
        
        if(anchorBs.getLength() < 1)
            throw new RuntimeException("missing \"anchor_b\" node in \"revolute_joint\"");
        if(anchorBs.getLength() > 1)
            throw new RuntimeException("multiple \"anchor_b\" nodes not allowed in \"revolute_joint\"");

        Node anchorANode = anchorAs.item(0);
        if(anchorANode.getNodeType() != Node.ELEMENT_NODE)
            throw new RuntimeException("invalid \"anchor_a\" node");
        Element anchorAElement = (Element) anchorANode;
        Vector2 anchorA = parseVectorElement(anchorAElement);
        
        Node anchorBNode = anchorBs.item(0);
        if(anchorBNode.getNodeType() != Node.ELEMENT_NODE)
            throw new RuntimeException("invalid \"anchor_b\" node");
        Element anchorBElement = (Element) anchorBNode;
        Vector2 anchorB = parseVectorElement(anchorBElement);
        
        joint.setBodyA(entity.bodies.get(HashHelper.hash32(nameBodyA)));
        joint.setBodyB(entity.bodies.get(HashHelper.hash32(nameBodyB)));
        joint.setCollisionEnabled(collision);
        joint.setAngle(rotation);
        joint.setAnchorA(anchorA);
        joint.setAnchorB(anchorB);
        
        return new Pair<String, PhysicsJoint>(name, joint);
    }
    
    private Pair<Integer, Mask> parseColoredLayerElement(Element coloredLayerElement)
    {
        int layerHeight = XMLHelper.getDeepValueInteger(coloredLayerElement, "layer_height");
        
        NodeList polygonShapes = coloredLayerElement.getElementsByTagName("polygon_shape");
        if(polygonShapes.getLength() < 1)
            throw new RuntimeException("missing \"polygon_shape\" node in \"colored_layer\"");
        if(polygonShapes.getLength() > 1)
            throw new RuntimeException("multiple \"polygon_shape\" nodes not allowed in \"colored_layer\"");
        Node polygonShapeNode = polygonShapes.item(0);
        if(polygonShapeNode.getNodeType() != Node.ELEMENT_NODE)
            throw new RuntimeException("invalid \"polygon_shape\" node");
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
            r[i] = ((vertex.color >> 24) & 0xFF) / (float) (0xFF);
            g[i] = ((vertex.color >> 16) & 0xFF) / (float) (0xFF);
            b[i] = ((vertex.color >> 8 ) & 0xFF) / (float) (0xFF);
            a[i] = ((vertex.color      ) & 0xFF) / (float) (0xFF);
        }
        
        ColoredMask mask = new ColoredMask(n, x, y, r, g, b, a);
        return new Pair<Integer, Mask>(layerHeight, mask);
    }
    
    private Pair<String, Mask> parseMaskElement(Element maskElement, Entity entity)
    {
        LayeredMask mask = new LayeredMask(RenderingOrder.BOTTOM_TO_TOP);
        
        String name   = XMLHelper.getDeepValue(maskElement, "name");
        
        Map<Integer, Mask> layers = new HashMap<Integer, Mask>();
        
        NodeList coloredLayers = maskElement.getElementsByTagName("colored_layer");
        
        for(int i = 0; i < coloredLayers.getLength(); i++)
        {
            Node coloredLayerNode = coloredLayers.item(i);
            if(coloredLayerNode.getNodeType() != Node.ELEMENT_NODE)
                throw new RuntimeException("invalid \"colored_layer\" node");
            Element coloredLayerElement = (Element) coloredLayerNode;
            Pair<Integer, Mask> layerData = parseColoredLayerElement(coloredLayerElement);
            if(layers.containsKey(layerData.first))
                throw new RuntimeException("duplicate layer height");
            layers.put(layerData.first, layerData.second);
        }
        
        Set<Integer> heights = layers.keySet();
        for(Integer height : heights)
            mask.addLayer(layers.get(height));
        
        return new Pair<String, Mask>(name, mask);
    }
    
    private Pair<String, String> parsePropertyElement(Element propertyElement)
    {
        String name = XMLHelper.getDeepValue(propertyElement, "name");
        String type = XMLHelper.getDeepValue(propertyElement, "type");
        return new Pair<String, String>(name, type);
    }
}
