package net.lodoma.lime.server.io.entity;

import java.io.IOException;

import net.lodoma.lime.physics.entity.Entity;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerOutput;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.util.Vector2;

public class SOEntityLinearImpulse extends ServerOutput
{
    public SOEntityLinearImpulse(Server server, String soName)
    {
        super(server, soName, Entity.class, Object.class, Vector2.class, Vector2.class);
    }
    
    @Override
    protected void localHandle(ServerUser user, Object... args) throws IOException
    {
        Entity entity = (Entity) args[0];
        int id = 0;
        if(args[1] instanceof Double) id = (int) ((double) ((Double) args[1]));
        else if(args[1] instanceof Integer) id = (Integer) args[1];
        else throw new IllegalArgumentException();

        Vector2 impulse = (Vector2) args[2];
        Vector2 point = (Vector2) args[3];
        
        user.outputStream.writeInt(entity.getID());
        user.outputStream.writeInt(id);
        user.outputStream.writeFloat(impulse.x);
        user.outputStream.writeFloat(impulse.y);
        user.outputStream.writeFloat(point.x);
        user.outputStream.writeFloat(point.y);
    }
}