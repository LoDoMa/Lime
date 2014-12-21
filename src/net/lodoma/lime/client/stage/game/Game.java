package net.lodoma.lime.client.stage.game;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientConnectionException;
import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.client.stage.StageManager;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.security.Credentials;
import net.lodoma.lime.world.gfx.WorldRenderer;

public class Game extends Stage
{
    private Client client;
    
    private String host;
    
    private Credentials credentials;
    
    public Game(StageManager manager, String host, Credentials credentials)
    {
        super(manager);
        this.host = host;
        this.credentials = credentials;
    }
    
    @Override
    public void preStart()
    {
        
    }
    
    @Override
    public void onStart()
    {
        client = new Client();
        
        try
        {
            client.open(19424, host, credentials);
        }
        catch(ClientConnectionException e)
        {
            Throwable cause = e.getCause();
            new GameMessage(manager, cause.getClass().getSimpleName() + ": " + cause.getMessage()).startStage();
        }
    }
    
    @Override
    public void onEnd()
    {
        if(client.isRunning())
            client.close();
    }
    
    @Override
    public void postEnd()
    {
        if(client.getCloseMessage() != null)
            new GameMessage(manager, client.getCloseMessage()).startStage();
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
                WorldRenderer renderer = (WorldRenderer) client.getProperty("worldRenderer");
                renderer.render();
            }
    }
}
