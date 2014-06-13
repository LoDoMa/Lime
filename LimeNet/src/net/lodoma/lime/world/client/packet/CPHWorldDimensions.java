package net.lodoma.lime.world.client.packet;

import java.nio.ByteBuffer;

import net.lodoma.lime.client.generic.net.GenericClient;
import net.lodoma.lime.client.generic.net.packet.ClientPacketHandler;
import net.lodoma.lime.world.ClientsideWorld;

public class CPHWorldDimensions extends ClientPacketHandler
{
    @Override
    public void handle(GenericClient client, byte[] data)
    {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        int width = buffer.getInt();
        int height = buffer.getInt();
        
        ClientsideWorld world = (ClientsideWorld) client.getProperty("world");
        world.receiveDimensions(width, height);
    }
}
