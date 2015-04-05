package net.lodoma.lime.client.stage.game;

import java.net.InetAddress;

import net.lodoma.lime.Lime;
import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientConnectionException;
import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.input.Input;

public class Game extends Stage
{
    private Client client;
    
    private InetAddress address;
    
    public Game(InetAddress address)
    {
        this.address = address;
    }
    
    @Override
    public void onActive()
    {
        super.onActive();
        
        client = new Client();
        
        try
        {
            client.open(address);
        }
        catch(ClientConnectionException e)
        {
            Lime.LOGGER.W("Client failed to connect");
            
            Throwable cause = e.getCause();
            manager.pop();
            manager.push(new GameMessage(cause.getClass().getSimpleName() + ": " + cause.getMessage()));
        }
    }
    
    @Override
    public void onInactive()
    {
        super.onInactive();
        
        if (client.isRunning())
        {
            client.worldRenderer.clean();
            client.close();
        }
    }
    
    @Override
    public void update(double timeDelta)
    {
        if (Input.getKey(Input.KEY_ESCAPE))
            client.close();
        
        if(!client.isRunning())
            manager.pop();
    }
    
    @Override
    public void render()
    {
        if(client.isRunning() && client.logic != null)
            client.logic.render();
    }
}
