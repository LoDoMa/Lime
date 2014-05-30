package net.lodoma.limemod.net.chat;

import java.util.HashSet;
import java.util.Set;

import net.lodoma.lime.client.generic.net.GenericClient;

public class ChatManager
{
    private GenericClient client;
    private Set<ChatHandler> handlers;
    
    public ChatManager(GenericClient client)
    {
        this.client = client;
        handlers = new HashSet<ChatHandler>();
    }
    
    public void addHandler(ChatHandler handler)
    {
        handlers.add(handler);
    }
    
    public void sendChatPacket(byte[] chat)
    {
        client.getData().packetPool.getPacket("Lime::ChatMessage").send(client, chat);
    }
    
    public void handleChatPacket(byte[] chat)
    {
        for(ChatHandler handler : handlers)
            handler.handle(chat);
    }
}
