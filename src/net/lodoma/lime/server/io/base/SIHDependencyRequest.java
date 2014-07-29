package net.lodoma.lime.server.io.base;

import java.io.IOException;

import net.lodoma.lime.common.NetStage;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerInputHandler;
import net.lodoma.lime.server.ServerOutput;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.server.dependency.DependencyPool;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.util.HashPool32;

public class SIHDependencyRequest extends ServerInputHandler
{
    public static final String NAME = "Lime::DependencyRequest";
    public static final int HASH = HashHelper.hash32(NAME);
    
    private DependencyPool pool;
    private HashPool32<ServerOutput> soPool;
    
    @SuppressWarnings("unchecked")
    public SIHDependencyRequest(Server server)
    {
        super(server, NetStage.DEPENDENCY);
        pool = (DependencyPool) server.getProperty("dependencyPool");
        soPool = (HashPool32<ServerOutput>) server.getProperty("soPool");
    }

    @Override
    protected void localHandle(ServerUser user) throws IOException
    {
        int index = user.inputStream.readInt();
        ServerOutput output = pool.getDependency(index);
        if(output == null)
            soPool.get(SONetworkStageChange.HASH).handle(user, NetStage.USER);
        else
            output.handle(user);
    }
}
