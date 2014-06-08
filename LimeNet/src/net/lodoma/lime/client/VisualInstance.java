package net.lodoma.lime.client;

import net.lodoma.lime.client.net.LimeClient;
import net.lodoma.lime.client.window.DisplayModeSearchException;
import net.lodoma.lime.client.window.InvalidWindowPropertyException;
import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.world.World;
import net.lodoma.lime.world.generator.WorldFileLoader;

public class VisualInstance
{
    private LimeClient client;
    
    private World world;
    
    private void init()
    {
        WorldFileLoader fileLoader = new WorldFileLoader();
        world = fileLoader.build();
        
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
            world.render();
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
