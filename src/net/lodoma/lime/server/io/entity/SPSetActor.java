package net.lodoma.lime.server.io.entity;

import java.io.IOException;
import java.util.Map;

import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerPacket;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.util.HashHelper;

public class SPSetActor extends ServerPacket
{
    public static final String NAME = "Lime::SetActor";
    public static final int HASH = HashHelper.hash32(NAME);
    
    private Map<Integer, Integer> actors;
    
    @SuppressWarnings("unchecked")
    public SPSetActor(Server server)
    {
        super(server, HASH, Integer.class);
        
        actors = (Map<Integer, Integer>) server.getProperty("actors");
    }
    
    @Override
    protected void localWrite(ServerUser user, Object... args) throws IOException
    {
        int userID = user.getID();
        int entityID = (Integer) args[0];
        
        actors.put(userID, entityID);
        
        user.outputStream.writeInt(entityID);
    }
}
