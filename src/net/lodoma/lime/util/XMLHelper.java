package net.lodoma.lime.util;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLHelper
{
    public static int getChildIntegerValue(Element parent, String nodeName)
    {
        return getNodeIntegerValue(getUniqueNode(parent, nodeName));
    }
    
    public static float getChildFloatValue(Element parent, String nodeName)
    {
        return getNodeFloatValue(getUniqueNode(parent, nodeName));
    }
    
    public static double getChildDoubleValue(Element parent, String nodeName)
    {
        return getNodeDoubleValue(getUniqueNode(parent, nodeName));
    }
    
    public static String getChildValue(Element parent, String nodeName)
    {
        return getNodeValue(getUniqueNode(parent, nodeName));
    }
    
    public static int getNodeIntegerValue(Node node)
    {
        try { return Integer.parseInt(getNodeValue(node)); }
        catch(NumberFormatException e)
        {
            throw new RuntimeException("value of \"" + node.getNodeName() + "\" must be an integer");
        }
    }
    
    public static float getNodeFloatValue(Node node)
    {
        return (float) getNodeDoubleValue(node);
    }
    
    public static double getNodeDoubleValue(Node node)
    {
        try { return Double.parseDouble(getNodeValue(node)); }
        catch(NumberFormatException e)
        {
            throw new RuntimeException("value of \"" + node.getNodeName() + "\" must be a floating point");
        }
    }
    
    public static String getNodeValue(Node node)
    {
        NodeList childNodes = node.getChildNodes();
        if(childNodes.getLength() > 1)
            throw new RuntimeException("can't read value - multiple child nodes");
        Node textNode = childNodes.item(0);
        if(textNode.getNodeType() != Node.TEXT_NODE)
            throw new RuntimeException("can't read value - child node must be a text node");
        return textNode.getNodeValue().trim();
    }
    
    public static Element getUniqueElement(Element parent, String nodeName)
    {
        return toElement(getUniqueNode(parent, nodeName));
    }
    
    public static Node getUniqueNode(Element parent, String nodeName)
    {
        NodeList childNodes = parent.getChildNodes();
        int length = childNodes.getLength();
        
        Node node = null;
        for(int i = 0; i < length; i++)
        {
            Node current = childNodes.item(i);
            if(current.getNodeName().equals(nodeName))
            {
                if(node != null)
                    throw new RuntimeException("\"" + nodeName + "\" in \"" + parent.getNodeName() + "\" must be unique");
                node = current;
            }
        }
        
        if(node == null)
            throw new RuntimeException("\"" + nodeName + "\" not found in \"" + parent.getNodeName() + "\"");
        return node;
    }
    
    public static boolean hasChild(Element parent, String nodeName)
    {
        NodeList childNodes = parent.getChildNodes();
        int length = childNodes.getLength();
        for(int i = 0; i < length; i++)
            if(childNodes.item(i).getNodeName().equals(nodeName))
                return true;
        return false;
    }
    
    public static Element[] getChildElementsByName(Element parent, String name)
    {
        Node[] nodes = getChildNodesByName(parent, name);
        Element[] elements = new Element[nodes.length];
        for(int i = 0; i < nodes.length; i++)
            elements[i] = toElement(nodes[i]);
        return elements;
    }
    
    public static Node[] getChildNodesByName(Element parent, String name)
    {
        List<Node> nodeList = new ArrayList<Node>();
        
        NodeList childNodes = parent.getChildNodes();
        int length = childNodes.getLength();
        for(int i = 0; i < length; i++)
        {
            Node current = childNodes.item(i);
            if(current.getNodeName().equals(name))
                nodeList.add(current);
        }
        
        Node[] nodes = new Node[nodeList.size()];
        nodeList.toArray(nodes);
        return nodes;
    }
    
    public static Element toElement(Node node)
    {
        if(node.getNodeType() != Node.ELEMENT_NODE)
            throw new RuntimeException("\"" + node.getNodeName() + "\" must be an element node");
        return (Element) node;
    }
}
