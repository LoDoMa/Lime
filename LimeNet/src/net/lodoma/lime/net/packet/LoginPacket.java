package net.lodoma.lime.net.packet;

import net.lodoma.lime.net.client.generic.GenericClient;
import net.lodoma.lime.net.packet.generic.NetStage;
import net.lodoma.lime.net.packet.generic.PacketHandler;
import net.lodoma.lime.net.server.generic.GenericServer;
import net.lodoma.lime.net.server.generic.ServerUser;

public class LoginPacket extends PacketHandler
{
    public LoginPacket()
    {
        super(0, NetStage.PRIMITIVE);
    }
    
    @Override
    public void handle(GenericClient client, byte[] data)
    {
        client.setProperty("accepted", true);
    }
    
    @Override
    public void handle(GenericServer server, byte[] data, ServerUser user)
    {
        user.stage = NetStage.ACCEPTED;
        server.sendData(buildMessage(new byte[]
        { (byte) 0x00 }), user);
    }
}
