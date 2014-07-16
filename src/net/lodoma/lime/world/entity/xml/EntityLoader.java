package net.lodoma.lime.world.entity.xml;

import java.io.File;
import java.io.IOException;

import javax.naming.InvalidNameException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.lodoma.lime.physics.PhysicsBody;
import net.lodoma.lime.physics.PhysicsBodyType;
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
        
        String name         = XMLHelper.getDeepValue(doc.getDocumentElement(), "name");
        String visualName   = XMLHelper.getDeepValue(doc.getDocumentElement(), "visual");
        
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
            parseBodyElement((Element) bodies.item(i));
        }
    }
    
    private Vertex parseVertex(Element vertexElement)
    {
        Vertex vertex = new Vertex();

        if(XMLHelper.hasNode(vertexElement, "index"))
            vertex.index = XMLHelper.getDeepValueInteger(vertexElement, "index");
        
        if(XMLHelper.hasNode(vertexElement, "x")) vertex.x = XMLHelper.getDeepValueFloat(vertexElement, "x");
        if(XMLHelper.hasNode(vertexElement, "y")) vertex.y = XMLHelper.getDeepValueFloat(vertexElement, "y");
        
        if(XMLHelper.hasNode(vertexElement, "color"))
            vertex.color = XMLHelper.getDeepValueInteger(vertexElement, "color");
        
        return vertex;
    }
    
    private void parseBodyElement(Element bodyElement)
    {
        PhysicsBody body = new PhysicsBody();
        
        String name     = XMLHelper.getDeepValue(bodyElement, "name");
        String type     = XMLHelper.getDeepValue(bodyElement, "type");
        String behavior = XMLHelper.getDeepValue(bodyElement, "behavior");
        
        if(type == "circle")
        {
            float radius = XMLHelper.getDeepValueFloat(bodyElement, "radius");
            body.setCircleShape(radius);
        }
        
        if(type == "polygon")
        {
            int vertexCount = XMLHelper.getDeepValueInteger(bodyElement, "vertex_count");
            Vector2[] vertices = new Vector2[vertexCount];
            
            NodeList vertexNodes = bodyElement.getElementsByTagName("vertex");
            for(int i = 0; i < vertexNodes.getLength(); i++)
            {
                Node vertexNode = vertexNodes.item(i);
                if(vertexNode.getNodeType() != Node.ELEMENT_NODE)
                    throw new RuntimeException("invalid \"vertex\" node");
                Element vertexElement = (Element) vertexNode;
                Vertex vertex = parseVertex(vertexElement);
                vertices[vertex.index - 1] = new Vector2(vertex.x, vertex.y);
            }
            
            body.setPolygonShape(vertices);
        }
        
        if(behavior == "dynamic")
        {
            body.setBodyType(PhysicsBodyType.DYNAMIC);
            
            float density     = XMLHelper.getDeepValueFloat(bodyElement, "density");
            float friction    = XMLHelper.getDeepValueFloat(bodyElement, "friction");
            float restitution = XMLHelper.getDeepValueFloat(bodyElement, "restitution");
            
            body.setDensity(density);
            body.setFriction(friction);
            body.setRestitution(restitution);
        }
    }
}
