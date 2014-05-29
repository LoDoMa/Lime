package net.lodoma.lime.client.net.packet;

import java.nio.ByteBuffer;

import net.lodoma.lime.client.generic.net.GenericClient;
import net.lodoma.lime.client.generic.net.packet.ClientPacketHandler;
import net.lodoma.lime.client.generic.net.packet.ClientPacketPool;
import net.lodoma.lime.mod.ModulePool;

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
        ModulePool modulePool = (ModulePool) client.getProperty("modulePool");
        ClientPacketPool packetPool = (ClientPacketPool) client.getProperty("packetPool");
        packetPool.getPacket("Lime::DependencyRequest").send(client, modulePool.isModuleLoaded(name));
    }
}
