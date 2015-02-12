package net.lodoma.lime.client.stage.game;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientConnectionException;
import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.input.Input;

public class Game extends Stage
{
    private Client client;
    
    private String host;
    
    public Game(String host)
    {
        this.host = host;
    }
    
    @Override
    public void onActive()
    {
        super.onActive();
        
        client = new Client();
        
        try
        {
            client.open(host);
        }
        catch(ClientConnectionException e)
        {
            Throwable cause = e.getCause();
            manager.pop();
            manager.push(new GameMessage(cause.getClass().getSimpleName() + ": " + cause.getMessage()));
        }
    }
    
    @Override
    public void onInactive()
    {
        super.onInactive();
        
        if(client.isRunning())
        {
            client.worldRenderer.clean();
            client.close();
        }
        
        if(client.getCloseMessage() != null)
            manager.push(new GameMessage(client.getCloseMessage()));
    }
    
    @Override
    public void update(double timeDelta)
    {
        if(Input.getKey(Input.KEY_ESCAPE))
            client.close();
        
        if(!client.isRunning())
            manager.pop();
    }
    
    @Override
    public void render()
    {
        if(client.isRunning())
            if(client.worldRenderer != null)
                client.worldRenderer.render();
    }
}
