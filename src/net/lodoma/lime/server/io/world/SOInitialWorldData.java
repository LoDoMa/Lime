package net.lodoma.lime.server.io.world;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerOutput;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.world.platform.Platform;
import net.lodoma.lime.world.server.ServersideWorld;

public class SOInitialWorldData extends ServerOutput
{
    public SOInitialWorldData(Server server, String soName)
    {
        super(server, soName);
    }
    
    @Override
    protected void localHandle(ServerUser user, Object... args) throws IOException
    {
        ServersideWorld world = (ServersideWorld) server.getProperty("world");
        
        List<Platform> platforms = world.getPlatformList();
        user.outputStream.writeInt(platforms.size());
        for(Platform platform : platforms)
            platform.writeToStream(user.outputStream);
        
        Set<Integer> entityIDSet = world.getEntityIDSet();
        user.outputStream.writeInt(entityIDSet.size());
        for(int entityID : entityIDSet)
        {
            user.outputStream.writeInt(entityID);
            user.outputStream.writeLong(world.getEntity(entityID).getHash());
        }
    }
}
