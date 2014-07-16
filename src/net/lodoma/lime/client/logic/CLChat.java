package net.lodoma.lime.client.logic;

import net.lodoma.lime.chat.ChatConsole;
import net.lodoma.lime.chat.ChatManager;
import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientInputHandler;
import net.lodoma.lime.client.ClientOutput;
import net.lodoma.lime.client.io.chat.CIHChatMessageReceive;
import net.lodoma.lime.client.io.chat.COChatMessageSend;
import net.lodoma.lime.util.HashPool;
import net.lodoma.lime.util.ThreadHelper;

public class CLChat implements ClientLogic
{
    private Client client;
    private HashPool<ClientInputHandler> cihPool;
    private HashPool<ClientOutput> coPool;
    
    private ChatConsole chatConsole;
    private ChatManager chatManager;
    
    @Override
    public void baseInit(Client client)
    {
        this.client = client;
    }

    @Override
    public void propertyInit()
    {
        client.setProperty("chatManager", new ChatManager(client));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void fetchInit()
    {
        cihPool = (HashPool<ClientInputHandler>) client.getProperty("cihPool");
        coPool = (HashPool<ClientOutput>) client.getProperty("coPool");
        chatManager = (ChatManager) client.getProperty("chatManager");
    }

    @Override
    public void generalInit()
    {
        cihPool.add("Lime::ChatMessageReceive", new CIHChatMessageReceive(client));
        coPool.add("Lime::ChatMessageSend", new COChatMessageSend(client, "Lime::ChatMessageSend"));
        
        chatConsole = new ChatConsole(client);
        chatConsole.start();
        
        chatManager.addHandler(chatConsole);
    }

    @Override
    public void clean()
    {
        try
        {
            ThreadHelper.interruptAndWait(chatConsole);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void logic()
    {
        
    }
}
