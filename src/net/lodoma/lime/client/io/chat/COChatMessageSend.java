package net.lodoma.lime.client.io.chat;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientOutput;

public class COChatMessageSend extends ClientOutput
{
    public COChatMessageSend(Client client, String coName)
    {
        super(client, coName, String.class);
    }

    @Override
    protected void localHandle(Object... args) throws IOException
    {
        outputStream.writeChars((String) args[0]);
        outputStream.writeChar(0);
    }
}
