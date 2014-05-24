package net.lodoma.lime.net.packet.dependency;

import net.lodoma.lime.net.client.generic.GenericClient;
import net.lodoma.lime.net.packet.generic.ClientPacketHandler;

public class CPHUserStatus extends ClientPacketHandler
{
    @Override
    public void handle(GenericClient client, byte[] data)
    {
        System.err.println("I'm a useeer! :D");
    }
}
