package net.lodoma.lime.server.io.world;

import java.io.IOException;

import net.lodoma.lime.physics.entity.Entity;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerOutput;
import net.lodoma.lime.server.ServerUser;

public class SOEntityCorrection extends ServerOutput
{
    public SOEntityCorrection(Server server, String soName)
    {
        super(server, soName, Entity.class);
    }
    
    @Override
    protected void localHandle(ServerUser user, Object... args) throws IOException
    {
        Entity entity = (Entity) args[0];
        user.outputStream.writeInt(entity.getID());
        entity.sendCorrection(user.outputStream);
    }
}
