package net.lodoma.lime.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatConsole implements Runnable, ChatSender, ChatReceiver
{
    private Thread thread;
    private boolean running;
    
    private List<String> toSend;
    private List<ChatManager> managers;
    
    public ChatConsole()
    {
        toSend = Collections.synchronizedList(new ArrayList<String>());
        managers = Collections.synchronizedList(new ArrayList<ChatManager>());
        
        running = true;
        thread = new Thread(this);
        thread.start();
    }
    
    @Override
    public void run()
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while(running)
            try
            {
                while(System.in.available() > 0)
                    toSend.add(reader.readLine());
                Thread.sleep(10);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            catch(InterruptedException e)
            {
                
            }
    }
    
    @Override
    public void addChatManager(ChatManager manager)
    {
        managers.add(manager);
    }
    
    @Override
    public void sendChat()
    {
        List<String> sent = new ArrayList<String>();
        for(String str : toSend)
        {
            for(ChatManager manager : managers)
                manager.send(str);
            sent.add(str);
        }
        toSend.removeAll(sent);
    }
    
    @Override
    public void receiveChat(String message)
    {
        System.out.println("chat: " + message);
    }
    
    @Override
    public void closeChatSender()
    {
        running = false;
    }
    
    @Override
    public void closeChatReceiver()
    {
        
    }
}
