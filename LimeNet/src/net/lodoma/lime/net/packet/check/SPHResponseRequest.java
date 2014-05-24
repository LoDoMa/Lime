package net.lodoma.lime.net.packet.check;

import net.lodoma.lime.net.packet.generic.ServerPacketHandler;
import net.lodoma.lime.net.packet.generic.ServerPacketPool;
import net.lodoma.lime.net.server.generic.GenericServer;
import net.lodoma.lime.net.server.generic.ServerUser;
import net.lodoma.lime.util.NetStage;

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
