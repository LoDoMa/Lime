package net.lodoma.lime.util;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLHelper
{
    public static boolean getChildBooleanValue(Element parent, String nodeName) throws XMLHelperException
    {
        return getNodeBooleanValue(getUniqueNode(parent, nodeName));
    }
    
    public static int getChildIntegerValue(Element parent, String nodeName) throws XMLHelperException
    {
        return getNodeIntegerValue(getUniqueNode(parent, nodeName));
    }
    
    public static float getChildFloatValue(Element parent, String nodeName) throws XMLHelperException
    {
        return getNodeFloatValue(getUniqueNode(parent, nodeName));
    }
    
    public static double getChildDoubleValue(Element parent, String nodeName) throws XMLHelperException
    {
        return getNodeDoubleValue(getUniqueNode(parent, nodeName));
    }
    
    public static String getChildValue(Element parent, String nodeName) throws XMLHelperException
    {
        return getNodeValue(getUniqueNode(parent, nodeName));
    }
    
    public static boolean getNodeBooleanValue(Node node) throws XMLHelperException
    {
        try { return Boolean.parseBoolean(getNodeValue(node)); }
        catch(NumberFormatException e)
        {
            throw new XMLHelperException("value of \"" + node.getNodeName() + "\" must be a boolean");
        }
    }
    
    public static int getNodeIntegerValue(Node node) throws XMLHelperException
    {
        try { return Integer.parseInt(getNodeValue(node)); }
        catch(NumberFormatException e)
        {
            throw new XMLHelperException("value of \"" + node.getNodeName() + "\" must be an integer");
        }
    }
    
    public static float getNodeFloatValue(Node node) throws XMLHelperException
    {
        return (float) getNodeDoubleValue(node);
    }
    
    public static double getNodeDoubleValue(Node node) throws XMLHelperException
    {
        try { return Double.parseDouble(getNodeValue(node)); }
        catch(NumberFormatException e)
        {
            throw new XMLHelperException("value of \"" + node.getNodeName() + "\" must be a floating point");
        }
    }
    
    public static String getNodeValue(Node node) throws XMLHelperException
    {
        NodeList childNodes = node.getChildNodes();
        if(childNodes.getLength() > 1)
            throw new XMLHelperException("can't read value - multiple child nodes");
        Node textNode = childNodes.item(0);
        if(textNode.getNodeType() != Node.TEXT_NODE)
            throw new XMLHelperException("can't read value - child node must be a text node");
        return textNode.getNodeValue().trim();
    }
    
    public static Element getUniqueElement(Element parent, String nodeName) throws XMLHelperException
    {
        return toElement(getUniqueNode(parent, nodeName));
    }
    
    public static Node getUniqueNode(Element parent, String nodeName) throws XMLHelperException
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
                    throw new XMLHelperException("\"" + nodeName + "\" in \"" + parent.getNodeName() + "\" must be unique");
                node = current;
            }
        }
        
        if(node == null)
            throw new XMLHelperException("\"" + nodeName + "\" not found in \"" + parent.getNodeName() + "\"");
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
    
    public static Element[] getChildElementsByName(Element parent, String name) throws XMLHelperException
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
    
    public static Element toElement(Node node) throws XMLHelperException
    {
        if(node.getNodeType() != Node.ELEMENT_NODE)
            throw new XMLHelperException("\"" + node.getNodeName() + "\" must be an element node");
        return (Element) node;
    }
}
