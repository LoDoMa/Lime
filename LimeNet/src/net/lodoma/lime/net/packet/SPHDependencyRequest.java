package net.lodoma.lime.net.packet;

import net.lodoma.lime.net.packet.dependency.DependencyPool;
import net.lodoma.lime.net.packet.generic.ServerPacketHandler;
import net.lodoma.lime.net.packet.generic.ServerPacketPool;
import net.lodoma.lime.net.server.generic.GenericServer;
import net.lodoma.lime.net.server.generic.ServerUser;

public class SPHDependencyRequest extends ServerPacketHandler
{
    @Override
    public void handle(GenericServer server, ServerUser user, byte[] data)
    {
        DependencyPool dependencyPool = (DependencyPool) server.getProperty("dependencyPool");
        if (data[0] == 0)
        {
            if (dependencyPool.hasNextDependency(user))
                dependencyPool.nextDependency(user).send(server, user);
            else
            {
                dependencyPool.endUserProgress(user);
                ((ServerPacketPool) server.getProperty("packetPool")).getPacket("Lime::UserStatus").send(server, user);
            }
        }
        else
            dependencyPool.endUserProgress(user);
    }
}
