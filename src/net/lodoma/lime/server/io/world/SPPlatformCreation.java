package net.lodoma.lime.server.io.world;

import java.io.IOException;

import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerPacket;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.world.platform.Platform;

public class SPPlatformCreation extends ServerPacket
{
    public static final String NAME = "Lime::PlatformCreation";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public SPPlatformCreation(Server server)
    {
        super(server, HASH, Platform.class);
    }
    
    @Override
    protected void localWrite(ServerUser user, Object... args) throws IOException
    {
        Platform platform = (Platform) args[0];
        platform.writeToStream(user.outputStream);
    }
}
