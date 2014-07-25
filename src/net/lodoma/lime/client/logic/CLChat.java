package net.lodoma.lime.client.logic;

import net.lodoma.lime.chat.ChatManager;
import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientInputHandler;
import net.lodoma.lime.client.ClientOutput;
import net.lodoma.lime.client.io.chat.CIHChatMessageReceive;
import net.lodoma.lime.client.io.chat.COChatMessageSend;
import net.lodoma.lime.util.HashPool;

public class CLChat implements ClientLogic
{
    private Client client;
    private HashPool<ClientInputHandler> cihPool;
    private HashPool<ClientOutput> coPool;
    
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
        chatManager.generalInit();
    }

    @Override
    public void clean()
    {
        chatManager.close();
    }

    @Override
    public void logic()
    {
        chatManager.send();
    }
}
