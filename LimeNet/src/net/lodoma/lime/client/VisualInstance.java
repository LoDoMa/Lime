package net.lodoma.lime.client;

import net.lodoma.lime.client.net.LimeClient;
import net.lodoma.lime.client.window.DisplayModeSearchException;
import net.lodoma.lime.client.window.InvalidWindowPropertyException;
import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.world.ClientsideWorld;

public class VisualInstance
{
    private LimeClient client;
    
    private void init()
    {
        client = new LimeClient();
        
        Window.setDimensions(800, 600);
        Window.setFullscreen(false);
        Window.setTitle("Lime");
        Window.setFPS(60);
        
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
        
        client.open(19523, "localhost");
    }
    
    private void loop()
    {
        while(!Window.isCloseRequested())
        {
            Window.clear();
            
            if(client.hasProperty("world"))
            {
                ClientsideWorld world = (ClientsideWorld) client.getProperty("world");
                world.render();
            }
            
            try
            {
                Window.update();
            }
            catch (InvalidWindowPropertyException e)
            {
                e.printStackTrace();
            }
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
