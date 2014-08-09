package net.lodoma.lime.util;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLHelper
{
    public static boolean hasNode(Element element, String tag)
    {
        return element.getElementsByTagName(tag).getLength() > 0;
    }
    
    public static float getDeepValueFloat(Element element, String tag)
    {
        String value = getDeepValue(element, tag);
        try
        {
            return Float.parseFloat(value);
        }
        catch(NumberFormatException e)
        {
            throw new RuntimeException("\"" + tag + "\" in \"" + element.getNodeName() + "\" is expected to be a floating-point");
        }
    }
    
    public static int getDeepValueInteger(Element element, String tag)
    {
        String value = getDeepValue(element, tag);
        try
        {
            if(value.startsWith("0x"))
                return (int) Long.parseLong(value.substring(2), 16);
            else
                return Integer.parseInt(value);
        }
        catch(NumberFormatException e)
        {
            throw new RuntimeException("\"" + tag + "\" in \"" + element.getNodeName() + "\" is expected to be an integer");
        }
    }
    
    public static boolean getDeepValueBoolean(Element element, String tag)
    {
        String value = getDeepValue(element, tag);
        try
        {
            return Boolean.parseBoolean(value);
        }
        catch(NumberFormatException e)
        {
            throw new RuntimeException("\"" + tag + "\" in \"" + element.getNodeName() + "\" is expected to be a boolean");
        }
    }
    
    public static String getDeepValue(Element element, String tag)
    {
        Node uniqueNode = getUniqueNode(element, tag);
        NodeList childNodes = uniqueNode.getChildNodes();
        return childNodes.item(0).getNodeValue().trim();
    }
    
    public static Element getUniqueElement(Element element, String tag)
    {
        return nodeToElement(getUniqueNode(element, tag));
    }
    
    public static Element getUniqueElement(Element element, String name, String[] tags)
    {
        return nodeToElement(getUniqueNode(element, name, tags));
    }
    
    public static Element nodeToElement(Node node)
    {
        if(node.getNodeType() != Node.ELEMENT_NODE)
            throw new RuntimeException("\"" + node.getNodeName() + "\" is expected to be an element node");
        return (Element) node;
    }
    
    public static Node getUniqueNode(Element element, String tag)
    {
        return getUniqueNode(element, "\"" + tag + "\"", new String[] { tag });
    }
    
    public static Node getUniqueNode(Element element, String name, String[] tags)
    {
        Node node = null;
        
        for(int i = 0; i < tags.length; i++)
        {
            NodeList list = element.getElementsByTagName(tags[i]);
            int length = list.getLength();

            if((length + (node == null ? 0 : 1)) > 1)
                throw new RuntimeException("multiple " + name + " nodes not allowed in \"" + element.getNodeName() + "\"");
            if(length == 1)
                node = list.item(0);
        }
        
        if(node == null)
            throw new RuntimeException("missing " + name + " in \"" + element.getNodeName() + "\"");
        return node;
    }
}
