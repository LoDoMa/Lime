package net.lodoma.lime.mod.limemod.client;

import net.lodoma.lime.mod.limemod.chat.ChatConsole;
import net.lodoma.lime.mod.limemod.chat.ChatManager;
import net.lodoma.lime.net.LogLevel;
import net.lodoma.lime.net.Logic;
import net.lodoma.lime.net.client.generic.GenericClient;

public class LimeModuleLogic implements Logic
{
    private ChatConsole console;
    private GenericClient client;
    
    public LimeModuleLogic(GenericClient client)
    {
        this.client = client;
    }

    @Override
    public void onOpen()
    {
        console = new ChatConsole(client);
        
        ChatManager chatManager = (ChatManager) client.getProperty("chatManager");
        chatManager.addHandler(console);
        
        console.start();
    }

    @Override
    public void onClose()
    {
        console.interrupt();
        while(console.isAlive())
            try
            {
                Thread.sleep(1);
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
