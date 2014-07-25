package net.lodoma.lime.client.stage.game;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientConnectionException;
import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.client.stage.StageManager;
import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.world.client.ClientsideWorld;

public class Game extends Stage
{
    private Client client;
    
    public Game(StageManager manager)
    {
        super(manager);
    }
    
    @Override
    public void onStart()
    {
        client = new Client();
        
        try
        {
            client.open(19424, "localhost");
        }
        catch(ClientConnectionException e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onEnd()
    {
        if(client.isRunning())
            client.close();
    }
    
    @Override
    public void update(double timeDelta)
    {
        if(Input.getKey(Input.KEY_ESCAPE))
            client.close();
        
        if(!client.isRunning())
            endStage();
    }
    
    @Override
    public void render()
    {
        if(client.isRunning())
            if(client.hasProperty("world"))
            {
                ClientsideWorld world = (ClientsideWorld) client.getProperty("world");
                world.render();
            }
    }
}
