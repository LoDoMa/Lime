package net.lodoma.lime.client.io.entity;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacketHandler;
import net.lodoma.lime.physics.entity.Entity;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.world.client.ClientsideWorld;

public class CPHEntityCorrection extends ClientPacketHandler
{
    public static final String NAME = "Lime::EntityCorrection";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public CPHEntityCorrection(Client client)
    {
        super(client);
    }

    @Override
    protected void localHandle() throws IOException
    {
        int entityID = inputStream.readInt();
        
        Entity entity = ((ClientsideWorld) client.getProperty("world")).getEntity(entityID);
        if(entity == null || !entity.isCreated()) return;
        
        entity.receiveCorrection(inputStream);
    }
}
