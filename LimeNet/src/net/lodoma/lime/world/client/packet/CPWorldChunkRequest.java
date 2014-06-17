package net.lodoma.lime.world.client.packet;

import java.nio.ByteBuffer;

import net.lodoma.lime.client.generic.net.packet.ClientPacket;

public class CPWorldChunkRequest extends ClientPacket
{
    public CPWorldChunkRequest()
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
