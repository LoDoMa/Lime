package net.lodoma.lime.server.net.packet;

import net.lodoma.lime.common.net.NetStage;
import net.lodoma.lime.common.net.packet.DependencyPool;
import net.lodoma.lime.server.generic.GenericServer;
import net.lodoma.lime.server.generic.ServerUser;
import net.lodoma.lime.server.generic.net.packet.ServerPacketHandler;
import net.lodoma.lime.server.generic.net.packet.ServerPacketPool;

public class SPHDependencyRequest extends ServerPacketHandler
{
    public SPHDependencyRequest()
    {
        super(NetStage.DEPENDENCY);
    }

    @Override
    protected void handle(GenericServer server, ServerUser user, byte[] data)
    {
        DependencyPool dependencyPool = (DependencyPool) server.getProperty("dependencyPool");
        if (data[0] == 0)
        {
            if (dependencyPool.hasNextDependency(user))
                dependencyPool.nextDependency(user).send(server, user);
            else
            {
                user.stage = NetStage.USER;
                dependencyPool.endUserProgress(user);
                ((ServerPacketPool) server.getProperty("packetPool")).getPacket("Lime::UserStatus").send(server, user);
            }
        }
        else
            dependencyPool.endUserProgress(user);
    }
}
