package net.lodoma.lime.mod.limemod.chat;

import java.nio.ByteBuffer;

import net.lodoma.lime.net.packet.generic.ClientPacket;

public class CPChatMessage extends ClientPacket
{
    public CPChatMessage()
    {
        super(String.class);
    }
    
    @Override
    protected byte[] build(Object... args)
    {
        String message = (String) args[0];
        int length = message.length();
        ByteBuffer buffer = ByteBuffer.allocate(length + 4);
        buffer.putInt(length);
        buffer.put(message.getBytes());
        return buffer.array();
    }
}
