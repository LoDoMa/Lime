package net.lodoma.lime.net.client;

import net.lodoma.lime.net.client.generic.ClientPacketPool;
import net.lodoma.lime.net.packet.CPConnectRequest;
import net.lodoma.lime.net.packet.CPHConnectRequestAnswer;


public class ClientStart
{
    public static void main(String[] args)
    {
        LimeClient client = new LimeClient();
        client.open(19523, "localhost", new LimeClientLogic());
        
        ClientPacketPool packetPool = (ClientPacketPool) client.getProperty("packetPool");
        packetPool.addPacket("Lime::ConnectRequest", new CPConnectRequest());
        packetPool.addHandler("Lime::ConnectRequestAnswer", new CPHConnectRequestAnswer());
        
        packetPool.getPacket("Lime::ConnectRequest").send(client);
    }
}
