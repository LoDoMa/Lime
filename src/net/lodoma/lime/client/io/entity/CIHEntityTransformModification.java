package net.lodoma.lime.client.io.entity;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientInputHandler;
import net.lodoma.lime.physics.PhysicsBody;
import net.lodoma.lime.util.Vector2;
import net.lodoma.lime.world.client.ClientsideWorld;

public class CIHEntityTransformModification extends ClientInputHandler
{
    public CIHEntityTransformModification(Client client)
    {
        super(client);
    }
    
    @Override
    protected void localHandle() throws IOException
    {
        int entityID = inputStream.readInt();
        int bodyID = inputStream.readInt();
        
        float posx = inputStream.readFloat();
        float posy = inputStream.readFloat();
        Vector2 pos = new Vector2(posx, posy);
        float angle = inputStream.readFloat();
        
        PhysicsBody body = ((ClientsideWorld) client.getProperty("world")).getEntity(entityID).getBody(bodyID);
        body.setPosition(pos);
        body.setAngle(angle);
    }
}
