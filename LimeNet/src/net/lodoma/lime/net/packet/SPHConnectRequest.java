package net.lodoma.lime.net.packet;

import net.lodoma.lime.net.NetStage;
import net.lodoma.lime.net.packet.generic.ServerPacket;
import net.lodoma.lime.net.packet.generic.ServerPacketHandler;
import net.lodoma.lime.net.packet.generic.ServerPacketPool;
import net.lodoma.lime.net.server.generic.GenericServer;
import net.lodoma.lime.net.server.generic.ServerUser;

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
