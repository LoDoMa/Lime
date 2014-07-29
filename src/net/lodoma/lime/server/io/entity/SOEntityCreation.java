package net.lodoma.lime.server.io.entity;

import java.io.IOException;

import net.lodoma.lime.physics.entity.Entity;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerOutput;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.util.HashHelper;

public class SOEntityCreation extends ServerOutput
{
    public static final String NAME = "Lime::EntityCreation";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public SOEntityCreation(Server server)
    {
        super(server, HASH, Entity.class);
    }
    
    @Override
    protected void localHandle(ServerUser user, Object... args) throws IOException
    {
        Entity entity = (Entity) args[0];
        user.outputStream.writeInt(entity.getID());
        user.outputStream.writeLong(entity.getHash());
    }
}
