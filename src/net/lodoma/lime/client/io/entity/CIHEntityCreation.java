package net.lodoma.lime.client.io.entity;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientInputHandler;
import net.lodoma.lime.physics.entity.Entity;
import net.lodoma.lime.physics.entity.EntityLoader;
import net.lodoma.lime.world.client.ClientsideWorld;

import org.xml.sax.SAXException;

public class CIHEntityCreation extends ClientInputHandler
{
    public CIHEntityCreation(Client client)
    {
        super(client);
    }
    
    @Override
    protected void localHandle() throws IOException
    {
        ClientsideWorld world = (ClientsideWorld) client.getProperty("world");
        EntityLoader entityLoader = (EntityLoader) client.getProperty("entityLoader");
        
        int id = inputStream.readInt();
        long hash = inputStream.readLong();
        
        try
        {
            Entity entity = entityLoader.loadFromXML(entityLoader.getXMLFileByHash(hash), world, client);
            entity.setID(id);
            world.addEntity(entity);
        }
        catch (SAXException e)
        {
            e.printStackTrace();
        }
        catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }
    }
}
