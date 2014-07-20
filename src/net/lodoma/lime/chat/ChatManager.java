package net.lodoma.lime.chat;

import java.util.ArrayList;
import java.util.List;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientOutput;
import net.lodoma.lime.util.HashPool;

public class ChatManager
{
    private Client client;
    private ClientOutput send;
    
    private List<ChatSender> senders;
    private List<ChatReceiver> receivers;
    
    public ChatManager(Client client)
    {
        this.client = client;
        
        senders = new ArrayList<ChatSender>();
        receivers = new ArrayList<ChatReceiver>();
    }
    
    @SuppressWarnings("unchecked")
    public void generalInit()
    {
        send = ((HashPool<ClientOutput>) client.getProperty("coPool")).get("Lime::ChatMessageSend");
    }
    
    public void addSender(ChatSender sender)
    {
        senders.add(sender);
        sender.addChatManager(this);
    }
    
    public void addReceiver(ChatReceiver receiver)
    {
        receivers.add(receiver);
    }
    
    public void send(String message)
    {
        send.handle(message);
    }
    
    public void send()
    {
        for(ChatSender sender : senders)
            sender.sendChat();
    }
    
    public void receive(String message)
    {
        for(ChatReceiver receiver : receivers)
            receiver.receiveChat(message);
    }
    
    public void close()
    {
        for(ChatSender sender : senders)
            sender.closeChatSender();
        
        for(ChatReceiver receiver : receivers)
            receiver.closeChatReceiver();
    }
}
