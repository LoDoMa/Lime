package net.lodoma.lime.physics.entity;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static net.lodoma.lime.util.XMLHelper.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class EntityLoader
{
    public static EntityType loadEntity(InputStream in) throws EntityLoaderException
    {
        try
        {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document document = docBuilder.parse(in);
            
            Element rootElement = document.getDocumentElement();
            rootElement.normalize();
            
            if(!rootElement.getNodeName().equals("entity"))
                throw new EntityLoaderException("root element must be named \"entity\"");
    
            String name = getChildValue(rootElement, "name");
            String version = getChildValue(rootElement, "version");
    
            Hitbox hitbox = HitboxLoader.loadHitbox(getUniqueElement(rootElement, "hitbox"));
            Model model = ModelLoader.loadModel(getUniqueElement(rootElement, "model"));
            
            String scriptFile = getChildValue(rootElement, "script");
            
            return new EntityType(name, version, model, hitbox, scriptFile);
        }
        catch(HitboxLoaderException | ModelLoaderException | IOException
            | ParserConfigurationException | SAXException | RuntimeException e)
        {
            throw new EntityLoaderException(e);
        }
    }
}
