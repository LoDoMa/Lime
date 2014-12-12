package net.lodoma.lime.server.io.entity;

import java.io.IOException;
import java.util.List;

import net.lodoma.lime.physics.Entity;
import net.lodoma.lime.physics.EntityCorrector;
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
        super(server, HASH, List.class);
    }
    
    @Override
    protected void localWrite(ServerUser user, Object... args) throws IOException
    {
        @SuppressWarnings("unchecked")
        List<Entity> entityList = (List<Entity>) args[0];
        
        user.outputStream.writeInt(entityList.size());
        
        for (Entity entity : entityList)
        {
            user.outputStream.writeInt(entity.identifier);
            EntityCorrector.outputCorrection(entity, user.outputStream);
        }
    }
}
