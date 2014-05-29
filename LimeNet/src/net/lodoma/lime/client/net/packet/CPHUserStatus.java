package net.lodoma.lime.client.net.packet;

import net.lodoma.lime.client.generic.net.GenericClient;
import net.lodoma.lime.client.generic.net.packet.ClientPacketHandler;

public class CPHUserStatus extends ClientPacketHandler
{
    @Override
    public void handle(GenericClient client, byte[] data)
    {
        System.err.println("I'm a useeer! :D");
    }
}
