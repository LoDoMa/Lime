package net.lodoma.lime.server.io.world;

import java.io.IOException;

import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerOutput;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.world.platform.Platform;

public class SOPlatformCreation extends ServerOutput
{
    public static final String NAME = "Lime::PlatformCreation";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public SOPlatformCreation(Server server)
    {
        super(server, HASH, Platform.class);
    }
    
    @Override
    protected void localHandle(ServerUser user, Object... args) throws IOException
    {
        Platform platform = (Platform) args[0];
        platform.writeToStream(user.outputStream);
    }
}
