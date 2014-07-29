package net.lodoma.lime.client.io.chat;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientOutput;
import net.lodoma.lime.util.HashHelper;

public class COChatMessageSend extends ClientOutput
{
    public static final String NAME = "Lime::ChatMessageSend";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public COChatMessageSend(Client client)
    {
        super(client, HASH, String.class);
    }

    @Override
    protected void localHandle(Object... args) throws IOException
    {
        outputStream.writeChars((String) args[0]);
        outputStream.writeChar(0);
    }
}
