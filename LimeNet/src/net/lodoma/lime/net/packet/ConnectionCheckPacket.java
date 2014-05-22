package net.lodoma.lime.net.packet;

import net.lodoma.lime.net.client.generic.GenericClient;
import net.lodoma.lime.net.packet.generic.NetStage;
import net.lodoma.lime.net.packet.generic.PacketHandler;
import net.lodoma.lime.net.server.generic.GenericServer;
import net.lodoma.lime.net.server.generic.ServerUser;

public class ConnectionCheckPacket extends PacketHandler
{
    public ConnectionCheckPacket()
    {
        super(NetStage.USER);
    }

    @Override
    public void handle(GenericClient client, byte[] data)
    {
        client.setProperty("lastConnectionTime", System.currentTimeMillis());
    }

    @Override
    public void handle(GenericServer server, byte[] data, ServerUser user)
    {
        sendHeader(server, user);
    }
}
