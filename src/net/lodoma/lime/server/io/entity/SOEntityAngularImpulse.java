package net.lodoma.lime.server.io.entity;

import java.io.IOException;

import net.lodoma.lime.physics.entity.Entity;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerOutput;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.util.HashHelper;

public class SOEntityAngularImpulse extends ServerOutput
{
    public static final String NAME = "Lime::EntityAngularImpulse";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public SOEntityAngularImpulse(Server server)
    {
        super(server, HASH, Entity.class, Object.class, Object.class);
    }
    
    @Override
    protected void localHandle(ServerUser user, Object... args) throws IOException
    {
        Entity entity = (Entity) args[0];        
        int id = 0;
        if(args[1] instanceof Double) id = (int) ((double) ((Double) args[1]));
        else if(args[1] instanceof Integer) id = (Integer) args[1];
        else throw new IllegalArgumentException();
        
        float impulse = 0;
        if(args[2] instanceof Double) impulse = (float) ((double) ((Double) args[2]));
        else if(args[2] instanceof Float) impulse = (Float) args[2];
        
        user.outputStream.writeInt(entity.getID());
        user.outputStream.writeInt(id);
        user.outputStream.writeFloat(impulse);
    }
}
