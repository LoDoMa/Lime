package net.lodoma.lime.server.io.base;

import java.io.IOException;

import net.lodoma.lime.common.net.NetStage;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerOutput;
import net.lodoma.lime.server.ServerUser;

public class SONetworkStageChange extends ServerOutput
{
    public SONetworkStageChange(Server server, String soName)
    {
        super(server, soName, NetStage.class);
    }
    
    @Override
    protected void localHandle(ServerUser user, Object... args) throws IOException
    {
        NetStage stage = (NetStage) args[0];
        user.stage = stage;
        user.outputStream.writeInt(stage.ordinal());
    }
}
