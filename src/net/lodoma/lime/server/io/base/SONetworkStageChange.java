package net.lodoma.lime.server.io.base;

import java.io.IOException;

import net.lodoma.lime.common.NetStage;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerOutput;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.server.event.EventBundle;
import net.lodoma.lime.server.event.EventManager;
import net.lodoma.lime.util.HashPool;

public class SONetworkStageChange extends ServerOutput
{
    private EventManager manager;

    @SuppressWarnings("unchecked")
    public SONetworkStageChange(Server server, String soName)
    {
        super(server, soName, NetStage.class);
        HashPool<EventManager> emanPool = (HashPool<EventManager>) server.getProperty("emanPool");
        manager = emanPool.get("Lime::onNewUser");
    }
    
    @Override
    protected void localHandle(ServerUser user, Object... args) throws IOException
    {
        NetStage stage = (NetStage) args[0];
        user.stage = stage;
        user.outputStream.writeInt(stage.ordinal());
        if(stage == NetStage.USER)
            manager.onEvent(new EventBundle(new String[] {"serverUser"}, new Object[] {user}));
    }
}
