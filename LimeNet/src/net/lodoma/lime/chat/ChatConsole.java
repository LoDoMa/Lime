package net.lodoma.lime.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import net.lodoma.lime.client.generic.net.GenericClient;
import net.lodoma.lime.common.net.LogLevel;

public class ChatConsole extends Thread implements ChatHandler
{
    private GenericClient client;
    private ChatManager chatManager;
    
    public ChatConsole(GenericClient client)
    {
        this.client = client;
        chatManager = (ChatManager) client.getProperty("chatManager");
    }
    
    @Override
    public void run()
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while(!isInterrupted())
            try
            {
                while(System.in.available() > 0)
                {
                    byte[] bytes = reader.readLine().getBytes();
                    chatManager.sendChatPacket(bytes);
                }
            }
            catch(IOException e)
            {
                client.log(LogLevel.SEVERE, e);
            }
    }
    
    @Override
    public void handle(byte[] chatMessage)
    {
        System.out.println("chat: " + new String(chatMessage));
    }
}
