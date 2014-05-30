package net.lodoma.limemod.net.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import net.lodoma.lime.client.generic.net.GenericClient;
import net.lodoma.lime.common.net.LogLevel;

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
            try
            {
                while(System.in.available() > 0)
                {
                    byte[] bytes = reader.readLine().getBytes();
                    client.getData().chatManager.sendChatPacket(bytes);
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
