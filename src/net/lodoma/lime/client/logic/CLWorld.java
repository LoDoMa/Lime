package net.lodoma.lime.client.logic;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacketHandler;
import net.lodoma.lime.client.packet.CPHSnapshot;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.util.HashPool32;
import net.lodoma.lime.util.Timer;
import net.lodoma.lime.world.World;
import net.lodoma.lime.world.gfx.WorldRenderer;

public class CLWorld implements ClientLogic
{
    private Client client;
    
    private World world;
    private HashPool32<ClientPacketHandler> cphPool;
    
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
    
    @SuppressWarnings("unchecked")
    @Override
    public void fetchInit()
    {
        world = (World) client.getProperty("world");
        cphPool = (HashPool32<ClientPacketHandler>) client.getProperty("cphPool");
    }
    
    @Override
    public void generalInit()
    {
        cphPool.add(CPHSnapshot.HASH, new CPHSnapshot(client));
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
