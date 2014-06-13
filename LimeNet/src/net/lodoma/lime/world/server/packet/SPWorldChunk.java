package net.lodoma.lime.world.server.packet;

import java.nio.ByteBuffer;

import net.lodoma.lime.server.generic.net.packet.ServerPacket;

public class SPWorldChunk extends ServerPacket
{
    public SPWorldChunk()
    {
        super(Integer.class, Integer.class, Integer.class, Integer.class, byte[].class);
    }
    
    @Override
    protected byte[] build(Object... args)
    {
        int x = (Integer) args[0];
        int y = (Integer) args[1];
        int w = (Integer) args[2];
        int h = (Integer) args[3];
        byte[] c = (byte[]) args[4];
        ByteBuffer buffer = ByteBuffer.allocate(c.length + 16);
        buffer.putInt(x);
        buffer.putInt(y);
        buffer.putInt(w);
        buffer.putInt(h);
        buffer.put(c);
        return buffer.array();
    }
}
