package net.lodoma.lime.mod.limemod.chat;

import net.lodoma.lime.net.client.generic.GenericClient;

public class ChatConsole extends Thread implements ChatHandler
{
    private GenericClient client;
    
    public ChatConsole(GenericClient client)
    {
        this.client = client;
    }
    
    @Override
    public void run()
    {
        while(!this.isInterrupted())
        {
            
        }
    }
    
    @Override
    public void handle(byte[] chatMessage)
    {
        System.out.println("chat: " + new String(chatMessage));
    }
}
