package net.lodoma.lime.client.generic.net.packet;

import net.lodoma.lime.client.generic.net.GenericClient;

public abstract class ClientPacketHandler
{
    public abstract void handle(GenericClient client, byte[] data);
}
