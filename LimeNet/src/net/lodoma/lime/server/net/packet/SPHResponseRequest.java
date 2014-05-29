package net.lodoma.lime.server.net.packet;

import net.lodoma.lime.common.net.NetStage;
import net.lodoma.lime.server.generic.GenericServer;
import net.lodoma.lime.server.generic.ServerUser;
import net.lodoma.lime.server.generic.net.packet.ServerPacketHandler;
import net.lodoma.lime.server.generic.net.packet.ServerPacketPool;

public class SPHResponseRequest extends ServerPacketHandler
{
    public SPHResponseRequest()
    {
        super(NetStage.USER);
    }

    @Override
    protected void handle(GenericServer server, ServerUser user, byte[] data)
    {
        ServerPacketPool packetPool = (ServerPacketPool) server.getProperty("packetPool");
        packetPool.getPacket("Lime::Response").send(server, user);
    }
}
