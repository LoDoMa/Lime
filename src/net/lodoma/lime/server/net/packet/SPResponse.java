package net.lodoma.lime.server.net.packet;

import net.lodoma.lime.server.generic.net.packet.ServerPacket;

public class SPResponse extends ServerPacket
{
    @Override
    protected byte[] build(Object... args)
    {
        return new byte[] {};
    }
}