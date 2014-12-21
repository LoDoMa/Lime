package net.lodoma.lime.world.entity;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.util.XMLHelper;
import net.lodoma.lime.util.XMLHelperException;

public class EntityType implements Identifiable<Integer>
{
    public int identifier;
    
    public String name;
    public String version;
    public String script;
    
    public EntityType(File file)
    {
        try
        {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document document = docBuilder.parse(file);
            
            Element entity = document.getDocumentElement();
            entity.normalize();
            
            if(!entity.getNodeName().equals("entity"))
                // TODO: throw something better
                throw new RuntimeException("root element must be named \"entity\"");
            
            name = XMLHelper.getChildValue(entity, "name");
            version = XMLHelper.getChildValue(entity, "version");
            script = XMLHelper.getChildValue(entity, "script");
            
            setIdentifier(HashHelper.hash32(name));
        }
        catch (ParserConfigurationException | SAXException | IOException | XMLHelperException e)
        {
            // TODO: handle this
            e.printStackTrace();
        }
    }
    
    @Override
    public Integer getIdentifier()
    {
        return identifier;
    }
    
    @Override
    public void setIdentifier(Integer identifier)
    {
        this.identifier = identifier;
    }
}
