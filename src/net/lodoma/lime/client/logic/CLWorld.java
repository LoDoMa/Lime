package net.lodoma.lime.client.logic;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacketHandler;
import net.lodoma.lime.client.io.entity.CPHEntityAngularImpulse;
import net.lodoma.lime.client.io.entity.CPHEntityCorrection;
import net.lodoma.lime.client.io.entity.CPHEntityCreation;
import net.lodoma.lime.client.io.entity.CPHEntityForce;
import net.lodoma.lime.client.io.entity.CPHEntityLinearImpulse;
import net.lodoma.lime.client.io.entity.CPHEntityTransformModification;
import net.lodoma.lime.client.io.entity.CPHSetActor;
import net.lodoma.lime.client.io.world.CPHPlatformCreation;
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
        cphPool.add(CPHPlatformCreation.HASH, new CPHPlatformCreation(client));
        
        cphPool.add(CPHEntityCreation.HASH, new CPHEntityCreation(client));
        cphPool.add(CPHEntityCorrection.HASH, new CPHEntityCorrection(client));
        cphPool.add(CPHEntityTransformModification.HASH, new CPHEntityTransformModification(client));
        cphPool.add(CPHEntityLinearImpulse.HASH, new CPHEntityLinearImpulse(client));
        cphPool.add(CPHEntityAngularImpulse.HASH, new CPHEntityAngularImpulse(client));
        cphPool.add(CPHEntityForce.HASH, new CPHEntityForce(client));
        
        cphPool.add(CPHSetActor.HASH, new CPHSetActor(client));
        
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
