package net.lodoma.lime.client.io.entity;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientInputHandler;
import net.lodoma.lime.physics.entity.Entity;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.world.client.ClientsideWorld;

public class CIHEntityCorrection extends ClientInputHandler
{
    public static final String NAME = "Lime::EntityCorrection";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public CIHEntityCorrection(Client client)
    {
        super(client);
    }

    @Override
    protected void localHandle() throws IOException
    {
        ClientsideWorld world = (ClientsideWorld) client.getProperty("world");
        
        int id = inputStream.readInt();
        Entity entity = world.getEntity(id);
        if(entity != null && entity.isCreated())
            entity.receiveCorrection(inputStream);
    }
}
