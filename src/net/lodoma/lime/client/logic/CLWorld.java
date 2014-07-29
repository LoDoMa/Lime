package net.lodoma.lime.client.logic;

import java.io.File;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientInputHandler;
import net.lodoma.lime.client.ClientOutput;
import net.lodoma.lime.client.io.entity.CIHEntityAngularImpulse;
import net.lodoma.lime.client.io.entity.CIHEntityCorrection;
import net.lodoma.lime.client.io.entity.CIHEntityCreation;
import net.lodoma.lime.client.io.entity.CIHEntityForce;
import net.lodoma.lime.client.io.entity.CIHEntityLinearImpulse;
import net.lodoma.lime.client.io.entity.CIHEntityTransformModification;
import net.lodoma.lime.client.io.world.CIHPlatformCreation;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.physics.entity.EntityLoader;
import net.lodoma.lime.util.HashPool32;
import net.lodoma.lime.util.Timer;
import net.lodoma.lime.world.client.ClientsideWorld;

public class CLWorld implements ClientLogic
{
    private Client client;
    private HashPool32<ClientInputHandler> cihPool;
    @SuppressWarnings("unused")
    private HashPool32<ClientOutput> coPool;
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
        cihPool = (HashPool32<ClientInputHandler>) client.getProperty("cihPool");
        coPool = (HashPool32<ClientOutput>) client.getProperty("coPool");
        world = (ClientsideWorld) client.getProperty("world");
        entityLoader = (EntityLoader) client.getProperty("entityLoader");
        
        world.fetchInit();
    }
    
    @Override
    public void generalInit()
    {
        cihPool.add(CIHPlatformCreation.HASH, new CIHPlatformCreation(client));
        
        cihPool.add(CIHEntityCreation.HASH, new CIHEntityCreation(client));
        cihPool.add(CIHEntityCorrection.HASH, new CIHEntityCorrection(client));
        cihPool.add(CIHEntityTransformModification.HASH, new CIHEntityTransformModification(client));
        cihPool.add(CIHEntityLinearImpulse.HASH, new CIHEntityLinearImpulse(client));
        cihPool.add(CIHEntityAngularImpulse.HASH, new CIHEntityAngularImpulse(client));
        cihPool.add(CIHEntityForce.HASH, new CIHEntityForce(client));
        
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
