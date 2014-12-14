package net.lodoma.lime.physics;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.util.XMLHelperException;
import net.lodoma.lime.world.CommonWorld;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import static net.lodoma.lime.util.XMLHelper.*;

public class EntityLoader
{
    public static EntityFactory loadEntityType(InputStream in, CommonWorld world) throws EntityLoaderException
    {
        try
        {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document document = docBuilder.parse(in);
            
            Element entity = document.getDocumentElement();
            entity.normalize();
            
            if(!entity.getNodeName().equals("entity"))
                throw new EntityLoaderException("root element must be named \"entity\"");

            EntityFactory factory = new EntityFactory();
            
            factory.name = getChildValue(entity, "name");
            factory.nameHash = HashHelper.hash32(factory.name);
            factory.version = getChildValue(entity, "version");
            factory.physicsConfig = loadPhysicsConfiguration(getUniqueElement(entity, "physics"));
            if (world.visualWorld != null)
                factory.shapeConfig = loadShapeConfiguration(getUniqueElement(entity, "shape"));
            factory.actorCapability = getChildBooleanValue(entity, "actor");
            factory.script = getChildValue(entity, "script");
            factory.world = world;
            
            factory.createEntityType();
            
            return factory;
        }
        catch (ParserConfigurationException | SAXException | XMLHelperException
             | EntityFactoryException | IllegalArgumentException | IOException e)
        {
            throw new EntityLoaderException(e);
        }
    }
    
    private static EntityFactory.PhysicsConfiguration loadPhysicsConfiguration(Element physics) throws XMLHelperException, IllegalArgumentException
    {
        EntityFactory.PhysicsConfiguration physicsConfig = new EntityFactory.PhysicsConfiguration();
        physicsConfig.restitution = getChildFloatValue(physics, "restitution");
        physicsConfig.density = getChildFloatValue(physics, "density");
        physicsConfig.bodyConfig = loadBodyConfiguration(getUniqueElement(physics, "body"));
        return physicsConfig;
    }
    
    private static EntityFactory.BodyConfiguration loadBodyConfiguration(Element body) throws XMLHelperException, IllegalArgumentException
    {
        EntityFactory.BodyConfiguration bodyConfig = new EntityFactory.BodyConfiguration();
        bodyConfig.type = Body.BodyType.valueOf(getChildValue(body, "type").toUpperCase());
        switch (bodyConfig.type)
        {
        case CIRCLE:
            bodyConfig.radius = getChildFloatValue(body, "radius");
            break;
        }
        return bodyConfig;
    }
    
    private static EntityFactory.ShapeConfiguration loadShapeConfiguration(Element shape) throws XMLHelperException, IllegalArgumentException
    {
        EntityFactory.ShapeConfiguration shapeConfig = new EntityFactory.ShapeConfiguration();
        shapeConfig.shapeComponentConfigList = new ArrayList<EntityFactory.ShapeComponentConfiguration>();
        Element[] componentArray = getChildElementsByName(shape, "component");
        for (Element component : componentArray)
            shapeConfig.shapeComponentConfigList.add(loadShapeComponentConfiguration(component));
        return shapeConfig;
    }
    
    private static EntityFactory.ShapeComponentConfiguration loadShapeComponentConfiguration(Element component) throws XMLHelperException, IllegalArgumentException
    {
        EntityFactory.ShapeComponentConfiguration shapeComponentConfig = new EntityFactory.ShapeComponentConfiguration();
        shapeComponentConfig.identifier = getChildIntegerValue(component, "id");
        shapeComponentConfig.type = ShapeComponent.ComponentType.valueOf(getChildValue(component, "type").toUpperCase());
        switch (shapeComponentConfig.type)
        {
        case CIRCLE:
            shapeComponentConfig.radius = getChildFloatValue(component, "radius");
            break;
        }
        return shapeComponentConfig;
    }
}
