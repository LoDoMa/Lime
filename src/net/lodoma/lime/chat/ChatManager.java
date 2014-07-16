package net.lodoma.lime.chat;

import java.util.HashSet;
import java.util.Set;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientOutput;
import net.lodoma.lime.util.HashPool;

public class ChatManager
{
    private Client client;
    private ClientOutput sendOutput;
    
    private Set<ChatHandler> handlers;
    
    public ChatManager(Client client)
    {
        this.client = client;
        handlers = new HashSet<ChatHandler>();
    }
    
    public void addHandler(ChatHandler handler)
    {
        handlers.add(handler);
    }
    
    public void send(String message)
    {
        if(sendOutput == null)
        {
            @SuppressWarnings("unchecked")
            HashPool<ClientOutput> coPool = (HashPool<ClientOutput>) client.getProperty("coPool");
            sendOutput = coPool.get("Lime::ChatMessageSend");
        }
        sendOutput.handle(message);
    }
    
    public void receive(String message)
    {
        for(ChatHandler handler : handlers)
            handler.receive(message);
    }
}
