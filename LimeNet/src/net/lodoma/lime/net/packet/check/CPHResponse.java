package net.lodoma.lime.net.packet.check;

import net.lodoma.lime.net.client.generic.GenericClient;
import net.lodoma.lime.net.packet.generic.ClientPacketHandler;

public class CPHResponse extends ClientPacketHandler
{
    @Override
    public void handle(GenericClient client, byte[] data)
    {
        client.setProperty("lastServerResponse", System.currentTimeMillis());
    }
}
