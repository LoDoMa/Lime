package net.lodoma.lime.chat;

import java.util.ArrayList;
import java.util.List;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacket;
import net.lodoma.lime.client.io.chat.CPChatMessageSend;
import net.lodoma.lime.util.HashPool32;

/**
 * ChatManager manages ChatSenders and ChatReceivers that are
 * added to it. The manager should be informed when a chat message is
 * received, so it can properly pass the received message to managed
 * receivers, and should be frequently updated.
 * 
 * @author Lovro Kalinovčić
 */
public class ChatManager
{
    private Client client;
    private ClientPacket send;  // packet used to send a message
    
    private List<ChatSender> senders;       // list of senders this manager is managing
    private List<ChatReceiver> receivers;   // list of receivers this manager is managing
    
    public ChatManager(Client client)
    {
        this.client = client;
        
        senders = new ArrayList<ChatSender>();
        receivers = new ArrayList<ChatReceiver>();
    }
    
    /**
     * Should be invoked on generalInit in client logic or after it.
     * Gets the ClientPacket for sending messages.
     */
    @SuppressWarnings("unchecked")
    public void generalInit()
    { 
        send = ((HashPool32<ClientPacket>) client.getProperty("cpPool")).get(CPChatMessageSend.HASH);
    }
    
    /**
     * Adds a sender to the list of managed senders,
     * then adds the manager to the sender.
     * @param sender - a sender to be added
     */
    public void addSender(ChatSender sender)
    {
        senders.add(sender);
        sender.addChatManager(this);
    }
    
    /**
     * Removes the manager from the sender,
     * then removes the sender from the list of managed senders.
     * @param sender - a sender to be removed
     */
    public void removeSender(ChatSender sender)
    {
        sender.removeChatManager(this);
        senders.remove(sender);
    }
    
    /**
     * Adds a receiver to the list of managed receivers.
     * @param receiver - a receiver to be added
     */
    public void addReceiver(ChatReceiver receiver)
    {
        receivers.add(receiver);
    }
    
    /**
     * Removes a receiver from the list of managed receivers.
     * @param receiver - a receiver to be removed
     */
    public void removeReceiver(ChatReceiver receiver)
    {
        receivers.remove(receiver);
    }
    
    /**
     * Sends a chat message to the server. This method
     * should only be invoked by chat senders.
     * @param message - a message to be sent
     */
    public void send(String message)
    {
        send.write(message);
    }
    
    /**
     * Updates all managed chat senders.
     */
    public void update()
    {
        for(ChatSender sender : senders)
            sender.update(this);
    }
    
    /**
     * Passes the received message to all managed receivers.
     * @param message - received message
     */
    public void receive(String message)
    {
        for(ChatReceiver receiver : receivers)
            receiver.receiveChat(this, message);
    }
    
    /**
     * Removes the manager from all managed senders.
     */
    public void close()
    {
        for(ChatSender sender : senders)
            sender.removeChatManager(this);
    }
}
