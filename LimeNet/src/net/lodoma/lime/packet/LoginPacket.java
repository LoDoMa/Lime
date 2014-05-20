package net.lodoma.lime.packet;

import net.lodoma.lime.client.generic.GenericClient;
import net.lodoma.lime.packet.generic.NetStage;
import net.lodoma.lime.packet.generic.PacketHandler;
import net.lodoma.lime.server.generic.GenericServer;
import net.lodoma.lime.server.generic.ServerUser;

public class LoginPacket extends PacketHandler
{
    public LoginPacket()
    {
        super(0, NetStage.PRIMITIVE);
    }
    
    @Override
    public void handle(GenericClient client, byte[] data)
    {
        if (data[0] == (byte) 0x00)
            System.out.println("accepted");
        else
            System.out.println("rejected");
    }
    
    @Override
    public void handle(GenericServer server, byte[] data, ServerUser user)
    {
        System.out.println("SERVER: a user asks for promotion");
        user.stage = NetStage.DEPENDENCY;
        server.sendData(buildMessage(new byte[]
        { (byte) 0x00 }), user);
    }
}
