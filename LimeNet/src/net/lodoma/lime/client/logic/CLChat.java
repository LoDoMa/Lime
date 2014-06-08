package net.lodoma.lime.client.logic;

import net.lodoma.lime.client.generic.net.GenericClient;
import net.lodoma.lime.common.net.LogLevel;
import net.lodoma.lime.util.ThreadHelper;
import net.lodoma.limemod.net.chat.ChatConsole;
import net.lodoma.limemod.net.chat.ChatManager;

public class CLChat implements ClientLogic
{
    private GenericClient client;
    
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
        chatManager = (ChatManager) client.getProperty("chatManager");
    }

    @Override
    public void generalInit()
    {
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
