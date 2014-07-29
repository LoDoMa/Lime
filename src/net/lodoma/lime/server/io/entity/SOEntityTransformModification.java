package net.lodoma.lime.server.io.entity;

import java.io.IOException;

import net.lodoma.lime.physics.PhysicsBody;
import net.lodoma.lime.physics.entity.Entity;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerOutput;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.util.Vector2;

public class SOEntityTransformModification extends ServerOutput
{
    public static final String NAME = "Lime::EntityTransformModification";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public SOEntityTransformModification(Server server)
    {
        super(server, HASH, Entity.class, Object.class);
    }
    
    @Override
    protected void localHandle(ServerUser user, Object... args) throws IOException
    {
        Entity entity = (Entity) args[0];
        int id = 0;
        if(args[1] instanceof Double) id = (int) ((double) ((Double) args[1]));
        else if(args[1] instanceof Integer) id = (Integer) args[1];
        else throw new IllegalArgumentException();
        
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
