package net.lodoma.lime.client.io.chat;

import java.io.IOException;

import net.lodoma.lime.chat.ChatManager;
import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacketHandler;
import net.lodoma.lime.util.HashHelper;

public class CPHChatMessageReceive extends ClientPacketHandler
{
    public static final String NAME = "Lime::ChatMessageReceive";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public CPHChatMessageReceive(Client client)
    {
        super(client);
    }

    @Override
    protected void localHandle() throws IOException
    {
        StringBuilder builder = new StringBuilder();
        char readChar;
        while((readChar = inputStream.readChar()) != 0)
            builder.append(readChar);
        
        String message = builder.toString();
        
        ChatManager chatManager = (ChatManager) client.getProperty("chatManager");
        chatManager.receive(message);
    }
}
