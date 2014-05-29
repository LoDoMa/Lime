package net.lodoma.lime.client.net.packet;

import net.lodoma.lime.client.generic.net.GenericClient;
import net.lodoma.lime.client.generic.net.packet.ClientPacketHandler;
import net.lodoma.lime.client.generic.net.packet.ClientPacketPool;

public class CPHConnectRequestAnswer extends ClientPacketHandler
{
    @Override
    public void handle(GenericClient client, byte[] data)
    {
        if(data[0] == 1)
        {
            System.err.println("accepted");
            
            ClientPacketPool packetPool = (ClientPacketPool) client.getProperty("packetPool");
            packetPool.getPacket("Lime::DependencyRequest").send(client, true);
        }
        else
        {
            System.err.println("rejected");
        }
    }
}
