package net.lodoma.lime.client.io.entity;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientInputHandler;
import net.lodoma.lime.world.client.ClientsideWorld;

public class CIHEntityAngularImpulse extends ClientInputHandler
{
    public CIHEntityAngularImpulse(Client client)
    {
        super(client);
    }
    
    @Override
    protected void localHandle() throws IOException
    {
        int entityID = inputStream.readInt();
        int bodyID = inputStream.readInt();
        float impulse = inputStream.readFloat();
        
        ((ClientsideWorld) client.getProperty("world")).getEntity(entityID).getBody(bodyID).applyAngularImpulse(impulse);
    }
}
