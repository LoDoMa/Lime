package net.lodoma.lime.client.io.chat;

import java.io.IOException;

import net.lodoma.lime.chat.ChatManager;
import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacketHandler;
import net.lodoma.lime.util.HashHelper;

/**
 * CPHChatMessageReceive is a ClientPacketHandler
 * that handles the SPChatMessageReceive packet.
 * 
 * This handler receives a chat message and passes
 * it to the ChatManager of the current chat session.
 * 
 * @author Lovro Kalinovčić
 */
public class CPHChatMessageReceive extends ClientPacketHandler
{
    /**
     * Unique name of this handler
     */
    public static final String NAME = "Lime::ChatMessageReceive";
    
    /**
     * 32-bit hash of this handlers name;
     */
    public static final int HASH = HashHelper.hash32(NAME);
    
    /**
     * 
     * @param client - the client that uses this handler
     */
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
