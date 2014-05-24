package net.lodoma.lime.net.packet.check;

import net.lodoma.lime.net.packet.generic.ServerPacket;

public class SPResponse extends ServerPacket
{
    @Override
    protected byte[] build(Object... args)
    {
        return new byte[] {};
    }
}
