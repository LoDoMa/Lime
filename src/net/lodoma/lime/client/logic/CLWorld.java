package net.lodoma.lime.client.logic;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.util.Timer;
import net.lodoma.lime.world.World;
import net.lodoma.lime.world.gfx.WorldRenderer;

public class CLWorld implements ClientLogic
{
    private Client client;
    
    private World world;
    
    private Timer timer;
    
    @Override
    public void baseInit(Client client)
    {
        this.client = client;
    }
    
    @Override
    public void propertyInit()
    {
        World world = new World();
        client.setProperty("world", world);
        client.setProperty("worldRenderer", new WorldRenderer(world));
    }
    
    @Override
    public void fetchInit()
    {
        world = (World) client.getProperty("world");
    }
    
    @Override
    public void generalInit()
    {
    }
    
    @Override
    public void clean()
    {
        world.clean();
    }
    
    @Override
    public void logic()
    {
        Input.update();
        
        if(timer == null) timer = new Timer();
        timer.update();
    }
}
