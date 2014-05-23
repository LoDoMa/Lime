package net.lodoma.lime.net.packet.generic;

import net.lodoma.lime.net.client.generic.GenericClient;

public abstract class ClientPacketHandler
{
    public abstract void handle(GenericClient client, byte[] data);
}
