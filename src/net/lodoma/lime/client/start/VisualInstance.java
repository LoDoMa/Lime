package net.lodoma.lime.client.start;

import java.io.File;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientConnectionException;
import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.util.FontHelper;
import net.lodoma.lime.world.client.ClientsideWorld;

public class VisualInstance
{
    private String host = "localhost";
    
    private Client client;
    
    private void init()
    {
        client = new Client();
        
        Window.setDimensions(800, 600);
        Window.setFullscreen(false);
        Window.setTitle("Lime");
        Window.setFPS(60);
        Window.setOrtho(10.0f, 10.0f);
        
        Window.open();
        
        FontHelper.registerFont(new File("fonts/mytype.ttf"));
        
        try
        {
            client.open(19424, host);
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
            
            Window.update();
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
