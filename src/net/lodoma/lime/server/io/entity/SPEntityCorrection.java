package net.lodoma.lime.server.io.entity;

import java.io.IOException;

import net.lodoma.lime.physics.entity.Entity;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerPacket;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.util.HashHelper;

public class SPEntityCorrection extends ServerPacket
{
    public static final String NAME = "Lime::EntityCorrection";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public SPEntityCorrection(Server server)
    {
        super(server, HASH, Entity.class);
    }
    
    @Override
    protected void localWrite(ServerUser user, Object... args) throws IOException
    {
        Entity entity = (Entity) args[0];
        user.outputStream.writeInt(entity.getID());
        entity.sendCorrection(user.outputStream);
    }
}