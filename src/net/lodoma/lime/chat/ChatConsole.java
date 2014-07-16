package net.lodoma.lime.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import net.lodoma.lime.client.Client;

public class ChatConsole extends Thread implements ChatHandler
{
    private Client client;
    private ChatManager chatManager;
    
    public ChatConsole(Client client)
    {
        this.client = client;
        chatManager = (ChatManager) this.client.getProperty("chatManager");
    }
    
    @Override
    public void run()
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while(!isInterrupted())
            try
            {
                while(System.in.available() > 0)
                    chatManager.send(reader.readLine());
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
    }
    
    @Override
    public void receive(String message)
    {
        System.out.println("chat: " + message);
    }
}
