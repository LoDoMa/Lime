package net.lodoma.lime.world.client.packet;

import net.lodoma.lime.client.generic.net.GenericClient;
import net.lodoma.lime.client.generic.net.packet.ClientPacketHandler;
import net.lodoma.lime.world.client.ClientsideWorld;

public class CPHWorldChunkInformation extends ClientPacketHandler
{
    @Override
    public void handle(GenericClient client, byte[] data)
    {
        ((ClientsideWorld) client.getProperty("world")).receiveChunkInformation(data);
    }
}
