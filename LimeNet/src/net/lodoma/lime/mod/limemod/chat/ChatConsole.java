package net.lodoma.lime.mod.limemod.chat;

import java.util.Scanner;

import net.lodoma.lime.net.client.generic.GenericClient;

public class ChatConsole extends Thread implements ChatHandler
{
    private GenericClient client;
    
    public ChatConsole(GenericClient client)
    {
        this.client = client;
    }
    
    @SuppressWarnings("resource")
    @Override
    public void run()
    {
        Scanner scanner = new Scanner(System.in);
        while(!isInterrupted())
        {
            while(scanner.hasNextInt())
            {
                String message = "" + scanner.nextInt();
                byte[] bytes = message.getBytes();
                ChatManager chatManager = (ChatManager) client.getProperty("chatManager");
                chatManager.sendChatPacket(bytes);
            }
        }
    }
    
    @Override
    public void handle(byte[] chatMessage)
    {
        System.out.println("chat: " + new String(chatMessage));
    }
}
