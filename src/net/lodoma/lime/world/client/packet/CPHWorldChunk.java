package net.lodoma.lime.world.client.packet;

import java.nio.ByteBuffer;

import net.lodoma.lime.client.generic.net.GenericClient;
import net.lodoma.lime.client.generic.net.packet.ClientPacketHandler;
import net.lodoma.lime.world.client.ClientsideWorld;

public class CPHWorldChunk extends ClientPacketHandler
{
    @Override
    public void handle(GenericClient client, byte[] data)
    {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        int x = buffer.getInt();
        int y = buffer.getInt();
        int w = buffer.getInt();
        int h = buffer.getInt();
        byte[] c = new byte[buffer.remaining()];
        buffer.get(c);
        ClientsideWorld world = (ClientsideWorld) client.getProperty("world");
        world.receiveChunk(x, y, w, h, c);
    }
}
