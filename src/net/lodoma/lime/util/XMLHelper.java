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
    
    public static String getDeepValue(Element element, String tag)
    {
        NodeList list = element.getElementsByTagName(tag);
        if(list.getLength() < 1)
            throw new RuntimeException("missing \"" + tag + "\" node in \"" + element.getNodeName() + "\"");
        if(list.getLength() > 1)
            throw new RuntimeException("multiple \"" + tag + "\" nodes not allowed in \"" + element.getNodeName() + "\"");
        NodeList list2 = list.item(0).getChildNodes();
        return list2.item(0).getNodeValue().trim();
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
    
    public static Node getUniqueNode(Element element, String tag)
    {
        NodeList nodeList = element.getElementsByTagName(tag);
        if(nodeList.getLength() < 1)
            throw new RuntimeException("missing \"" + tag + "\" in \"" + element.getNodeName() + "\"");
        if(nodeList.getLength() > 1)
            throw new RuntimeException("multiple \"" + tag + "\" nodes not allowed in \"" + element.getNodeName() + "\"");
        return nodeList.item(0);
    }
}
