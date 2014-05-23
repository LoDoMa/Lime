package net.lodoma.lime.net.packet.dependencytest;

import net.lodoma.lime.net.packet.generic.ServerPacketHandler;
import net.lodoma.lime.net.server.generic.GenericServer;
import net.lodoma.lime.net.server.generic.ServerUser;

public class SPHTestDependencyAnswer extends ServerPacketHandler
{
    @Override
    public void handle(GenericServer server, ServerUser user, byte[] data)
    {
        user.dependencies++;
    }
}
