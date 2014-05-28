package net.lodoma.lime.mod.limemod.chat;

import net.lodoma.lime.net.client.generic.GenericClient;

public class ChatConsole extends Thread implements ChatHandler
{
    private GenericClient client;
    
    public ChatConsole(GenericClient client)
    {
        this.client = client;
        start();
    }
    
    @Override
    public void run()
    {
        
    }
    
    @Override
    public void handle(byte[] chatMessage)
    {
        
    }
}
