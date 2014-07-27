package net.lodoma.lime.server.io.entity;

import java.io.IOException;

import net.lodoma.lime.physics.PhysicsBody;
import net.lodoma.lime.physics.entity.Entity;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerOutput;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.util.Vector2;

public class SOEntityTransformModification extends ServerOutput
{
    public SOEntityTransformModification(Server server, String soName)
    {
        super(server, soName, Entity.class, Object.class);
    }
    
    @Override
    protected void localHandle(ServerUser user, Object... args) throws IOException
    {
        Entity entity = (Entity) args[0];
        int id = 0;
        if(args[1] instanceof Double)
        {
            double dathing = (Double) args[1];
            id = (int) dathing;
        }
        if(args[1] instanceof Integer) id = (Integer) args[1];
        
        PhysicsBody body = entity.getBody(id);
        Vector2 pos = body.getPosition();
        float angle = body.getAngle();

        user.outputStream.writeInt(entity.getID());
        user.outputStream.writeInt(id);
        user.outputStream.writeFloat(pos.x);
        user.outputStream.writeFloat(pos.y);
        user.outputStream.writeFloat(angle);
    }
}
