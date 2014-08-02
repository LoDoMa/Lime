package net.lodoma.lime.client.logic;

import java.io.File;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacketHandler;
import net.lodoma.lime.client.ClientPacket;
import net.lodoma.lime.client.io.entity.CPHEntityAngularImpulse;
import net.lodoma.lime.client.io.entity.CPHEntityCorrection;
import net.lodoma.lime.client.io.entity.CPHEntityCreation;
import net.lodoma.lime.client.io.entity.CPHEntityForce;
import net.lodoma.lime.client.io.entity.CPHEntityLinearImpulse;
import net.lodoma.lime.client.io.entity.CPHEntityTransformModification;
import net.lodoma.lime.client.io.entity.CPHSetActor;
import net.lodoma.lime.client.io.world.CPHPlatformCreation;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.physics.entity.EntityLoader;
import net.lodoma.lime.util.HashPool32;
import net.lodoma.lime.util.Timer;
import net.lodoma.lime.world.client.ClientsideWorld;

public class CLWorld implements ClientLogic
{
    private Client client;
    private HashPool32<ClientPacketHandler> cphPool;
    @SuppressWarnings("unused")
    private HashPool32<ClientPacket> cpPool;
    private ClientsideWorld world;
    private EntityLoader entityLoader;
    
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
        client.setProperty("entityLoader", new EntityLoader());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void fetchInit()
    {
        cphPool = (HashPool32<ClientPacketHandler>) client.getProperty("cphPool");
        cpPool = (HashPool32<ClientPacket>) client.getProperty("cpPool");
        world = (ClientsideWorld) client.getProperty("world");
        entityLoader = (EntityLoader) client.getProperty("entityLoader");
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
        
        entityLoader.addXMLFile("Lime::Zombie", new File("model/zombie.xml"));
        
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
        if(timer == null) timer = new Timer();
        timer.update();
        world.update(timer.getDelta());
        Input.update();
    }
}
