package net.lodoma.lime.mod.limemod.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import net.lodoma.lime.net.LogLevel;
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
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while(!isInterrupted())
        {
            try
            {
                byte[] bytes = reader.readLine().getBytes();
                ChatManager chatManager = (ChatManager) client.getProperty("chatManager");
                chatManager.sendChatPacket(bytes);
            }
            catch(IOException e)
            {
                client.log(LogLevel.SEVERE, e);
            }
        }
    }
    
    @Override
    public void handle(byte[] chatMessage)
    {
        System.out.println("chat: " + new String(chatMessage));
    }
}
