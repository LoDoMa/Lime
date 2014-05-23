package net.lodoma.lime.net.packet.dependencytest;

import net.lodoma.lime.net.client.generic.GenericClient;
import net.lodoma.lime.net.packet.generic.ClientPacketHandler;
import net.lodoma.lime.net.packet.generic.ClientPacketPool;

public class CPHTestDependency extends ClientPacketHandler
{
    @Override
    public void handle(GenericClient client, byte[] data)
    {
        ClientPacketPool packetPool = (ClientPacketPool) client.getProperty("packetPool");
        packetPool.getPacket("Lime::TestDependencyAnswer").send(client);
    }
}
