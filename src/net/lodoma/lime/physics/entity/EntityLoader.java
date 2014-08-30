package net.lodoma.lime.physics.entity;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static net.lodoma.lime.util.XMLHelper.*;
import net.lodoma.lime.util.XMLHelperException;
import net.lodoma.lime.world.CommonWorld;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class EntityLoader
{
    public static EntityType loadEntity(InputStream in, CommonWorld world) throws EntityLoaderException
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
            
            return loadEntity(rootElement, world);
        }
        catch(ParserConfigurationException | SAXException | IOException e)
        {
            throw new EntityLoaderException(e);
        }
    }
    
    private static EntityType loadEntity(Element entityElement, CommonWorld world) throws EntityLoaderException
    {
        try
        {
            String name = getChildValue(entityElement, "name");
            String version = getChildValue(entityElement, "version");
    
            Hitbox hitbox = HitboxLoader.loadHitbox(getUniqueElement(entityElement, "hitbox"));
            Model model = ModelLoader.loadModel(getUniqueElement(entityElement, "model"));
            
            String scriptFile = getChildValue(entityElement, "script");
            
            EntityType entityType = new EntityType(name, version, model, hitbox, scriptFile, world);
            return entityType;
        }
        catch(HitboxLoaderException | ModelLoaderException | IOException | XMLHelperException e)
        {
            throw new EntityLoaderException(e);
        }
    }
}
