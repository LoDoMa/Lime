package net.lodoma.lime.world.server.packet;

import java.nio.ByteBuffer;

import net.lodoma.lime.server.generic.net.packet.ServerPacket;
import net.lodoma.lime.world.server.ServersideWorld;

public class SPWorldDimensions extends ServerPacket
{
    public SPWorldDimensions()
    {
        super(ServersideWorld.class);
    }
    
    @Override
    protected byte[] build(Object... args)
    {
        ServersideWorld world = (ServersideWorld) args[0];
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putInt(world.getWidth());
        buffer.putInt(world.getHeight());
        return buffer.array();
    }
    
}
