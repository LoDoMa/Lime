package net.lodoma.lime.mod.limemod.chat;

import net.lodoma.lime.net.NetStage;
import net.lodoma.lime.net.packet.generic.ServerPacketHandler;
import net.lodoma.lime.net.packet.generic.ServerPacketPool;
import net.lodoma.lime.net.server.generic.GenericServer;
import net.lodoma.lime.net.server.generic.ServerUser;

public class SPHChatMessage extends ServerPacketHandler
{
    public SPHChatMessage()
    {
        super(NetStage.USER);
    }

    @Override
    protected void handle(GenericServer server, ServerUser user, byte[] data)
    {
        ServerPacketPool packetPool = (ServerPacketPool) server.getProperty("packetPool");
        packetPool.getPacket("Lime::ChatMessage").sendToAll(server, data);
    }
}
