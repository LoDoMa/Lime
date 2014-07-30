package net.lodoma.lime.client.io.chat;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacket;
import net.lodoma.lime.util.HashHelper;

public class CPChatMessageSend extends ClientPacket
{
    public static final String NAME = "Lime::ChatMessageSend";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public CPChatMessageSend(Client client)
    {
        super(client, HASH, String.class);
    }

    @Override
    protected void localWrite(Object... args) throws IOException
    {
        outputStream.writeChars((String) args[0]);
        outputStream.writeChar(0);
    }
}
