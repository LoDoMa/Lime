package net.lodoma.lime.world.client.packet;

import net.lodoma.lime.client.generic.net.packet.ClientPacket;

public class CPWorldChunksRequest extends ClientPacket
{
    @Override
    protected byte[] build(Object... args)
    {
        return new byte[] {};
    }
}
