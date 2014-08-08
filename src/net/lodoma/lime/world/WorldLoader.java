package net.lodoma.lime.world;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.lodoma.lime.script.LuaScript;
import net.lodoma.lime.util.XMLHelper;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class WorldLoader
{
    public void loadFromXML(File xmlFile, CommonWorld world) throws WorldLoaderException
    {
        try
        {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            Element docElement = doc.getDocumentElement();
            docElement.normalize();
            
            String rootName = docElement.getNodeName();
            if(rootName != "world")
                throw new WorldLoaderException("root of a world XML file must be named \"world\"");
            
            String name = XMLHelper.getDeepValue(docElement, "name");
            String version = XMLHelper.getDeepValue(docElement, "version");
            
            world.name = name;
            world.version = version;
            
            world.script = new LuaScript();
            world.script.setGlobal("WORLD", world);
            world.script.setGlobal("SCRIPT", world.script);
            world.script.require("script/strict/world");
            world.script.require("script/strict/sandbox");
            world.script.load(new File(XMLHelper.getDeepValue(docElement, "script")));
        }
        catch(IOException | SAXException | ParserConfigurationException e)
        {
            throw new WorldLoaderException(e);
        }
    }
}
