package net.lodoma.lime.client.logic;

import net.lodoma.lime.chat.ChatManager;
import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacketHandler;
import net.lodoma.lime.client.ClientPacket;
import net.lodoma.lime.client.io.chat.CPHChatMessageReceive;
import net.lodoma.lime.client.io.chat.CPChatMessageSend;
import net.lodoma.lime.util.HashPool32;

public class CLChat implements ClientLogic
{
    private Client client;
    private HashPool32<ClientPacketHandler> cphPool;
    private HashPool32<ClientPacket> cpPool;
    
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
        cphPool = (HashPool32<ClientPacketHandler>) client.getProperty("cphPool");
        cpPool = (HashPool32<ClientPacket>) client.getProperty("cpPool");
        chatManager = (ChatManager) client.getProperty("chatManager");
    }

    @Override
    public void generalInit()
    {
        cphPool.add(CPHChatMessageReceive.HASH, new CPHChatMessageReceive(client));
        cpPool.add(CPChatMessageSend.HASH, new CPChatMessageSend(client));
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
        chatManager.update();
    }
}
