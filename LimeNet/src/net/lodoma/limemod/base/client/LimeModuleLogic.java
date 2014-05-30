package net.lodoma.limemod.base.client;

import net.lodoma.lime.client.generic.net.GenericClient;
import net.lodoma.lime.common.net.LogLevel;
import net.lodoma.lime.common.net.Logic;
import net.lodoma.lime.util.ThreadHelper;
import net.lodoma.limemod.net.chat.ChatConsole;

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
        
        client.getData().chatManager.addHandler(console);
        
        console.start();
    }

    @Override
    public void onClose()
    {
        try
        {
            ThreadHelper.interruptAndWait(console);
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
