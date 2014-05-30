package net.lodoma.limemod.net.chat.packet.client;

import java.nio.ByteBuffer;

import javax.xml.bind.DatatypeConverter;

import net.lodoma.lime.client.generic.net.GenericClient;
import net.lodoma.lime.client.generic.net.packet.ClientPacketHandler;

public class CPHChatMessage extends ClientPacketHandler
{
    @Override
    public void handle(GenericClient client, byte[] data)
    {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        int length = buffer.getInt();
        byte[] msg = new byte[length];
        buffer.get(msg);
        byte[] message = DatatypeConverter.parseBase64Binary(new String(msg));
        client.getData().chatManager.handleChatPacket(message);
    }
}
