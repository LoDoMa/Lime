package net.lodoma.lime.world.server.packet;

import java.nio.ByteBuffer;

import net.lodoma.lime.server.generic.net.packet.ServerPacket;

public class SPWorldChunkInformation extends ServerPacket
{
    public SPWorldChunkInformation()
    {
        super(Integer.class);
    }
    
    @Override
    protected byte[] build(Object... args)
    {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt((Integer) args[0]);
        return buffer.array();
    }
}
