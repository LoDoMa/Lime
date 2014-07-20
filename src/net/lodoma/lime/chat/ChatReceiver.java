package net.lodoma.lime.chat;

public interface ChatReceiver
{
    public void receiveChat(String message);
    public void closeChatReceiver();
}
