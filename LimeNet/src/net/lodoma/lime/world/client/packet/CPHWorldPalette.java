package net.lodoma.lime.world.client.packet;

import net.lodoma.lime.client.generic.net.GenericClient;
import net.lodoma.lime.client.generic.net.packet.ClientPacketHandler;
import net.lodoma.lime.world.ClientsideWorld;

public class CPHWorldPalette extends ClientPacketHandler
{
    @Override
    public void handle(GenericClient client, byte[] data)
    {
        ClientsideWorld world = (ClientsideWorld) client.getProperty("world");
        world.receivePalette(data);
    }
}
