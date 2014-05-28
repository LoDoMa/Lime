package net.lodoma.lime.mod.limemod.chat;

import java.nio.ByteBuffer;

import javax.xml.bind.DatatypeConverter;

import net.lodoma.lime.net.packet.generic.ClientPacket;

public class CPChatMessage extends ClientPacket
{
    public CPChatMessage()
    {
        super(byte[].class);
    }
    
    @Override
    protected byte[] build(Object... args)
    {
        byte[] message = (byte[]) args[0];
        String toSend = DatatypeConverter.printBase64Binary(message);
        ByteBuffer buffer = ByteBuffer.allocate(toSend.length() + 4);
        buffer.putInt(toSend.length());
        buffer.put(toSend.getBytes());
        return buffer.array();
    }
}
