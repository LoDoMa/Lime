package net.lodoma.lime.world.entity.xml;

import java.io.File;
import java.io.IOException;

import javax.naming.InvalidNameException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.lodoma.lime.physics.PhysicsBody;
import net.lodoma.lime.physics.PhysicsBodyType;
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
    
    public void loadFromXML(File xmlFile) throws IOException, SAXException, ParserConfigurationException
    {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        Element docElement = doc.getDocumentElement();
        docElement.normalize();
        
        String rootName = docElement.getNodeName();
        if(rootName != "model")
            new InvalidNameException("root of an entity XML file must be named \"model\"");
        
        String name         = XMLHelper.getDeepValue(docElement, "model_name");
        String visualName   = XMLHelper.getDeepValue(docElement, "model_visual");
        String version      = XMLHelper.getDeepValue(docElement, "model_version");
        
        NodeList bodies     = docElement.getElementsByTagName("body");
        NodeList joints     = docElement.getElementsByTagName("joint");
        NodeList masks      = docElement.getElementsByTagName("mask");
        NodeList properties = docElement.getElementsByTagName("property");
        NodeList scripts    = docElement.getElementsByTagName("script");

        for(int i = 0; i < bodies.getLength(); i++)
        {
            Node bodyNode = bodies.item(i);
            if(bodyNode.getNodeType() != Node.ELEMENT_NODE)
                throw new RuntimeException("invalid \"body\" node");
            Pair<String, PhysicsBody> bodyData = parseBodyElement((Element) bodies.item(i));
            
        }
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
    
    private Pair<String, PhysicsBody> parseBodyElement(Element bodyElement)
    {
        PhysicsBody body = new PhysicsBody();
        String name     = XMLHelper.getDeepValue(bodyElement, "name");
        
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
}
