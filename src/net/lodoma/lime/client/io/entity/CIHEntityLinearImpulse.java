package net.lodoma.lime.client.io.entity;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientInputHandler;
import net.lodoma.lime.util.Vector2;
import net.lodoma.lime.world.client.ClientsideWorld;

public class CIHEntityLinearImpulse extends ClientInputHandler
{
    public CIHEntityLinearImpulse(Client client)
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
        
        ((ClientsideWorld) client.getProperty("world")).getEntity(entityID).getBody(bodyID).applyLinearImpulse(impulse, point);
    }
}