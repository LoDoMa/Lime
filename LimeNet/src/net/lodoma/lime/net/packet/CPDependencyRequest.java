package net.lodoma.lime.net.packet;

import net.lodoma.lime.net.packet.generic.ClientPacket;

public class CPDependencyRequest extends ClientPacket
{
    @Override
    protected byte[] build(Object... args)
    {
        return new byte[] {};
    }
}
