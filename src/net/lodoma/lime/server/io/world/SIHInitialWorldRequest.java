package net.lodoma.lime.server.io.world;

import net.lodoma.lime.common.net.NetStage;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerInputHandler;
import net.lodoma.lime.server.ServerOutput;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.util.HashPool;

public class SIHInitialWorldRequest extends ServerInputHandler
{
    public SIHInitialWorldRequest(Server server)
    {
        super(server, NetStage.USER);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void localHandle(ServerUser user)
    {
        HashPool<ServerOutput> soPool = (HashPool<ServerOutput>) server.getProperty("soPool");
        soPool.get("Lime::InitialWorldData").handle(user);
    }
}
