package net.lodoma.lime.server.io.base;

import java.io.IOException;

import net.lodoma.lime.common.NetStage;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerInputHandler;
import net.lodoma.lime.server.ServerOutput;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.server.dependency.DependencyPool;
import net.lodoma.lime.util.HashPool;

public class SIHDependencyRequest extends ServerInputHandler
{
    private DependencyPool pool;
    private HashPool<ServerOutput> soPool;
    
    @SuppressWarnings("unchecked")
    public SIHDependencyRequest(Server server)
    {
        super(server, NetStage.DEPENDENCY);
        pool = (DependencyPool) server.getProperty("dependencyPool");
        soPool = (HashPool<ServerOutput>) server.getProperty("soPool");
    }

    @Override
    protected void localHandle(ServerUser user) throws IOException
    {
        int index = user.inputStream.readInt();
        ServerOutput output = pool.getDependency(index);
        if(output == null)
            soPool.get("Lime::NetworkStageChange").handle(user, NetStage.USER);
        else
            output.handle(user);
    }
}
