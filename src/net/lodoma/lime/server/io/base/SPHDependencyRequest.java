package net.lodoma.lime.server.io.base;

import java.io.IOException;

import net.lodoma.lime.common.NetStage;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerPacketHandler;
import net.lodoma.lime.server.ServerPacket;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.server.dependency.DependencyPool;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.util.HashPool32;

public class SPHDependencyRequest extends ServerPacketHandler
{
    public static final String NAME = "Lime::DependencyRequest";
    public static final int HASH = HashHelper.hash32(NAME);
    
    private DependencyPool pool;
    private HashPool32<ServerPacket> spPool;
    
    @SuppressWarnings("unchecked")
    public SPHDependencyRequest(Server server)
    {
        super(server, NetStage.DEPENDENCY);
        pool = (DependencyPool) server.getProperty("dependencyPool");
        spPool = (HashPool32<ServerPacket>) server.getProperty("spPool");
    }

    @Override
    protected void localHandle(ServerUser user) throws IOException
    {
        int index = user.inputStream.readInt();
        ServerPacket output = pool.getDependency(index);
        if(output == null)
            spPool.get(SPNetworkStageChange.HASH).write(user, NetStage.USER);
        else
            output.write(user);
    }
}
