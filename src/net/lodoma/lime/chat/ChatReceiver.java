package net.lodoma.lime.chat;

/**
 * Receives chat messages when added to a chat manager.
 * 
 * @author Lovro Kalinovčić
 */
public interface ChatReceiver
{
    /**
     * Invoked whenever a chat manager receives a message.
     * Can only be invoked by some chat manager if the chat receiver
     * has been added to it.
     * 
     * @param manager - a manager that received the message
     * @param message - received message
     */
    public void receiveChat(ChatManager manager, String message);
}
