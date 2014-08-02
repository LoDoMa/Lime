package net.lodoma.lime.client.io.entity;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacketHandler;
import net.lodoma.lime.physics.entity.Entity;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.util.Vector2;
import net.lodoma.lime.world.client.ClientsideWorld;

public class CPHEntityLinearImpulse extends ClientPacketHandler
{
    public static final String NAME = "Lime::EntityLinearImpulse";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public CPHEntityLinearImpulse(Client client)
    {
        super(client);
    }
    
    @Override
    protected void localHandle() throws IOException
    {
        int entityID = inputStream.readInt();
        int bodyID = inputStream.readInt();
        Vector2 impulse = new Vector2(inputStream.readFloat(), inputStream.readFloat());
        Vector2 point = new Vector2(inputStream.readFloat(), inputStream.readFloat());
        
        Entity entity = ((ClientsideWorld) client.getProperty("world")).getEntity(entityID);
        if(entity == null) return;
        
        entity.getBody(bodyID).applyLinearImpulse(impulse, point);
    }
}
