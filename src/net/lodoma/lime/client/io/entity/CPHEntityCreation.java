package net.lodoma.lime.client.io.entity;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacketHandler;
import net.lodoma.lime.physics.entity.Entity;
import net.lodoma.lime.physics.entity.EntityLoader;
import net.lodoma.lime.physics.entity.EntityLoaderException;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.world.client.ClientsideWorld;

public class CPHEntityCreation extends ClientPacketHandler
{
    public static final String NAME = "Lime::EntityCreation";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public CPHEntityCreation(Client client)
    {
        super(client);
    }
    
    @Override
    protected void localHandle() throws IOException
    {
        ClientsideWorld world = (ClientsideWorld) client.getProperty("world");
        EntityLoader entityLoader = (EntityLoader) client.getProperty("entityLoader");
        
        int id = inputStream.readInt();
        int hash = inputStream.readInt();
        
        try
        {
            Entity entity = entityLoader.newEntity(world, world.getPhysicsWorld(), client, hash);
            entity.setID(id);
            world.addEntity(entity);
        }
        catch (EntityLoaderException e)
        {
            if(e.getCause() == null) client.setCloseMessage("Failed to load entity: " + e.getMessage());
            else client.setCloseMessage("Failed to load entity: " + e.getCause().getClass().getCanonicalName());
            client.closeInThread();
        }
    }
}
