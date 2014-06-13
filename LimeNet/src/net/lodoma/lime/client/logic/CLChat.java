package net.lodoma.lime.client.logic;

import net.lodoma.lime.chat.ChatConsole;
import net.lodoma.lime.chat.ChatManager;
import net.lodoma.lime.chat.client.CPChatMessage;
import net.lodoma.lime.chat.client.CPHChatMessage;
import net.lodoma.lime.client.generic.net.GenericClient;
import net.lodoma.lime.client.generic.net.packet.ClientPacketPool;
import net.lodoma.lime.common.net.LogLevel;
import net.lodoma.lime.util.ThreadHelper;

public class CLChat implements ClientLogic
{
    private GenericClient client;
    private ClientPacketPool packetPool;
    
    private ChatConsole chatConsole;
    private ChatManager chatManager;
    
    @Override
    public void baseInit(GenericClient client)
    {
        this.client = client;
    }

    @Override
    public void propertyInit()
    {
        client.setProperty("chatManager", new ChatManager(client));
    }

    @Override
    public void fetchInit()
    {
        packetPool = (ClientPacketPool) client.getProperty("packetPool");
        chatManager = (ChatManager) client.getProperty("chatManager");
    }

    @Override
    public void generalInit()
    {
        packetPool.addPacket("Lime::ChatMessage", new CPChatMessage());
        packetPool.addHandler("Lime::ChatMessage", new CPHChatMessage());
        
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
            client.log(LogLevel.SEVERE, e);
        }
    }

    @Override
    public void logic()
    {
        
    }
}
