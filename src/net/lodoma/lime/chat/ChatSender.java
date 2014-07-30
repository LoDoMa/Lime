package net.lodoma.lime.chat;

/**
 * Sends chat messages when updated by a chat manager.
 * 
 * @author Lovro Kalinovčić
 */
public interface ChatSender
{
    /**
     * Invoked when the sender is added to a manager.
     * 
     * @param manager - a manager the sender has been added to.
     */
    public void addChatManager(ChatManager manager);
    
    /**
     * Invoked by a chat manager. When invoked, all messages that are
     * waiting to be sent should be sent to the chat manager
     * that invoked this method.
     * 
     * @param manager - a manager that invoked this method
     */
    public void update(ChatManager manager);
    
    /**
     * Invoked when the sender is removed from a manager,
     * or the manager closed.
     * 
     * @param manager - a manager the sander has been removed from.
     */
    public void removeChatManager(ChatManager manager);
}
