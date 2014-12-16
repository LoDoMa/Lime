package net.lodoma.lime.client.logic;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacketHandler;
import net.lodoma.lime.client.io.entity.CPHEntityCorrection;
import net.lodoma.lime.client.io.entity.CPHEntityCreation;
import net.lodoma.lime.client.io.entity.CPHSetActor;
import net.lodoma.lime.client.io.light.CPHLightCreation;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.util.HashPool32;
import net.lodoma.lime.util.Timer;
import net.lodoma.lime.world.client.ClientsideWorld;

public class CLWorld implements ClientLogic
{
    private Client client;
    private HashPool32<ClientPacketHandler> cphPool;
    private ClientsideWorld world;
    
    private Timer timer;
    
    @Override
    public void baseInit(Client client)
    {
        this.client = client;
    }
    
    @Override
    public void propertyInit()
    {
        client.setProperty("world", new ClientsideWorld(client));
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void fetchInit()
    {
        cphPool = (HashPool32<ClientPacketHandler>) client.getProperty("cphPool");
        world = (ClientsideWorld) client.getProperty("world");
    }
    
    @Override
    public void generalInit()
    {
        cphPool.add(CPHEntityCreation.HASH, new CPHEntityCreation(client));
        cphPool.add(CPHEntityCorrection.HASH, new CPHEntityCorrection(client));

        cphPool.add(CPHSetActor.HASH, new CPHSetActor(client));
        
        cphPool.add(CPHLightCreation.HASH, new CPHLightCreation(client));

        world.load();
        world.generalInit();
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
        world.update(timer.getDelta());
    }
}
