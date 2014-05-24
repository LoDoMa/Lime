package net.lodoma.lime.mod.limemod.chat;

import java.nio.ByteBuffer;

import javax.xml.bind.DatatypeConverter;

import net.lodoma.lime.net.client.generic.GenericClient;
import net.lodoma.lime.net.packet.generic.ClientPacketHandler;

public class CPHChatMessage extends ClientPacketHandler
{
    @Override
    public void handle(GenericClient client, byte[] data)
    {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        int length = buffer.getInt();
        byte[] msg = new byte[length];
        buffer.get(msg);
        String message = new String(DatatypeConverter.parseBase64Binary(new String(msg)));
        System.out.println("CHAT: " + message);
    }
}
