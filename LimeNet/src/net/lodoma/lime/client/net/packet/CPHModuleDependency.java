package net.lodoma.lime.client.net.packet;

import java.nio.ByteBuffer;

import net.lodoma.lime.client.ClientData;
import net.lodoma.lime.client.generic.net.GenericClient;
import net.lodoma.lime.client.generic.net.packet.ClientPacketHandler;

public class CPHModuleDependency extends ClientPacketHandler
{
    @Override
    public void handle(GenericClient client, byte[] data)
    {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        int length = buffer.getInt();
        byte[] nameBytes = new byte[length];
        buffer.get(nameBytes);
        String name = new String(nameBytes);
        ClientData cdata = client.getData();
        cdata.packetPool.getPacket("Lime::DependencyRequest").send(client, cdata.modulePool.isModuleLoaded(name));
    }
}
