package net.lodoma.lime.client.start;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientConnectionException;
import net.lodoma.lime.client.window.DisplayModeSearchException;
import net.lodoma.lime.client.window.InvalidWindowPropertyException;
import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.world.client.ClientsideWorld;

public class VisualInstance
{
    private String host = "localhost";
    private boolean connected;
    
    private Client client;
    
    private void init()
    {
        client = new Client();
        
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
        
        try
        {
            connected = false;
            client.open(19424, host);
            connected = true;
        }
        catch (ClientConnectionException e)
        {
            e.printStackTrace();
        }
    }
    
    private void loop()
    {
        while(client.isRunning() && !Window.isCloseRequested())
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
        if(connected)
        {
            if(client.isRunning())
                client.close();
        }
        Window.close();
    }
    
    public void run()
    {
        init();
        loop();
        clean();
    }
}
