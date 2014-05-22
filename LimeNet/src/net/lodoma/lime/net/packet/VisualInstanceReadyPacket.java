package net.lodoma.lime.net.packet;

import net.lodoma.lime.net.client.generic.GenericClient;
import net.lodoma.lime.net.packet.generic.NetStage;
import net.lodoma.lime.net.packet.generic.PacketHandler;
import net.lodoma.lime.net.server.generic.GenericServer;
import net.lodoma.lime.net.server.generic.ServerUser;

public class VisualInstanceReadyPacket extends PacketHandler
{
    public VisualInstanceReadyPacket()
    {
        super(NetStage.ACCEPTED);
    }

    @Override
    public void handle(GenericClient client, byte[] data)
    {
        
    }

    @Override
    public void handle(GenericServer server, byte[] data, ServerUser user)
    {
        user.stage = NetStage.USER;
    }
}
