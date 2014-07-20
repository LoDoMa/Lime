package net.lodoma.lime.chat;

public interface ChatSender
{
    public void addChatManager(ChatManager manager);
    public void sendChat();
    public void closeChatSender();
}
