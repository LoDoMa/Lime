package net.lodoma.lime.server.net.packet;

import net.lodoma.lime.common.net.NetStage;
import net.lodoma.lime.server.generic.GenericServer;
import net.lodoma.lime.server.generic.ServerUser;
import net.lodoma.lime.server.generic.net.packet.ServerPacket;
import net.lodoma.lime.server.generic.net.packet.ServerPacketHandler;
import net.lodoma.lime.server.generic.net.packet.ServerPacketPool;

public class SPHConnectRequest extends ServerPacketHandler
{
    public SPHConnectRequest()
    {
        super(NetStage.PRIMITIVE);
    }

    @Override
    protected void handle(GenericServer server, ServerUser user, byte[] data)
    {
        user.stage = NetStage.DEPENDENCY;
        ServerPacket packet = ((ServerPacketPool) server.getProperty("packetPool")).getPacket("Lime::ConnectRequestAnswer"); 
        packet.send(server, user, true);
    }
}
