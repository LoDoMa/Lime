package net.lodoma.lime.client.logic;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.packet.CPHSnapshot;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.util.Timer;
import net.lodoma.lime.world.World;
import net.lodoma.lime.world.gfx.WorldRenderer;

public class CLWorld implements ClientLogic
{
    private Client client;
    
    private Timer timer;
    
    @Override
    public void baseInit(Client client)
    {
        this.client = client;
    }
    
    @Override
    public void propertyInit()
    {
        client.world = new World();
        client.worldRenderer = new WorldRenderer(client.world);
    }
    
    @Override
    public void fetchInit()
    {
        
    }
    
    @Override
    public void generalInit()
    {
        client.cphPool.add(CPHSnapshot.HASH, new CPHSnapshot(client));
    }
    
    @Override
    public void clean()
    {
        client.world.clean();
    }
    
    @Override
    public void logic()
    {
        Input.update();
        
        if(timer == null) timer = new Timer();
        timer.update();
    }
}
