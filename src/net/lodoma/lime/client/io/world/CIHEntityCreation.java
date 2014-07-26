package net.lodoma.lime.client.io.world;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientInputHandler;
import net.lodoma.lime.event.EventManager;
import net.lodoma.lime.physics.entity.Entity;
import net.lodoma.lime.physics.entity.EntityLoader;
import net.lodoma.lime.util.HashPool32;
import net.lodoma.lime.world.client.ClientsideWorld;

import org.xml.sax.SAXException;

public class CIHEntityCreation extends ClientInputHandler
{
    public CIHEntityCreation(Client client)
    {
        super(client);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void localHandle() throws IOException
    {
        ClientsideWorld world = (ClientsideWorld) client.getProperty("world");
        EntityLoader entityLoader = (EntityLoader) client.getProperty("entityLoader");
        HashPool32<EventManager> emanPool = (HashPool32<EventManager>) client.getProperty("emanPool");
        
        int id = inputStream.readInt();
        long hash = inputStream.readLong();
        
        try
        {
            Entity entity = entityLoader.loadFromXML(entityLoader.getXMLFileByHash(hash), world, emanPool);
            entity.setID(id);
            entity.receiveCreation(inputStream);
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
