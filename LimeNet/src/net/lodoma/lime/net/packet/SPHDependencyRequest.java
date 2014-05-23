package net.lodoma.lime.net.packet;

import java.util.Set;

import net.lodoma.lime.net.packet.generic.ServerPacketHandler;
import net.lodoma.lime.net.packet.generic.ServerPacketPool;
import net.lodoma.lime.net.server.generic.GenericServer;
import net.lodoma.lime.net.server.generic.ServerUser;
import net.lodoma.lime.net.server.generic.UserPool;

public class SPHDependencyRequest extends ServerPacketHandler
{
    @Override
    public void handle(GenericServer server, ServerUser user, byte[] data)
    {
        ServerPacketPool packetPool = (ServerPacketPool) server.getProperty("packetPool");
        
        UserPool userPool = (UserPool) server.getProperty("userPool");
        Set<Long> dependencyList = userPool.getDependencyList();
        for(Long dependencyID : dependencyList)
            packetPool.getPacket(dependencyID).send(server, user);
        userPool.getWaitingUsers().add(user);
    }
}
