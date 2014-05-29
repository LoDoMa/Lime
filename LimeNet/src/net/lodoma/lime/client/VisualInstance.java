package net.lodoma.lime.client;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

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
        Window.setFullscreen(false);
        Window.setTitle("Lime");
        
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
        while(!Window.isCloseRequested())
        {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            GL11.glLoadIdentity();
            
            Display.update();
            Display.sync(60);
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
