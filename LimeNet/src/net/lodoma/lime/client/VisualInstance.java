package net.lodoma.lime.client;

import org.lwjgl.opengl.Display;

import net.lodoma.lime.client.net.LimeClient;
import net.lodoma.lime.client.net.LimeClientLogic;
import net.lodoma.lime.client.window.DisplayModeSearchException;
import net.lodoma.lime.client.window.InvalidWindowPropertyException;
import net.lodoma.lime.client.window.Window;

public class VisualInstance
{
    private LimeClient client;
    
    private void init()
    {
        client = new LimeClient();
        
        Window.setDimensions(800, 600);
        Window.setFullscreen(true);
        try
        {
            Window.open();
        }
        catch (InvalidWindowPropertyException e)
        {
            e.printStackTrace();
        }
        catch (DisplayModeSearchException e)
        {
            e.printStackTrace();
        }
        
        client.open(19523, "localhost", new LimeClientLogic());
    }
    
    private void loop()
    {
        while(!Display.isCloseRequested())
        {
            
        }
    }
    
    private void clean()
    {
        if(client.isRunning())
            client.close();
        
        Window.close();
    }
    
    public void run()
    {
        init();
        loop();
        clean();
    }
}
