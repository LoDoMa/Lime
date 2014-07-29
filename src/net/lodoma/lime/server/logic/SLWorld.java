package net.lodoma.lime.server.logic;

import net.lodoma.lime.physics.entity.EntityLoader;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerInputHandler;
import net.lodoma.lime.server.ServerOutput;
import net.lodoma.lime.server.io.entity.SOEntityAngularImpulse;
import net.lodoma.lime.server.io.entity.SOEntityCorrection;
import net.lodoma.lime.server.io.entity.SOEntityCreation;
import net.lodoma.lime.server.io.entity.SOEntityForce;
import net.lodoma.lime.server.io.entity.SOEntityLinearImpulse;
import net.lodoma.lime.server.io.entity.SOEntityTransformModification;
import net.lodoma.lime.server.io.world.SOPlatformCreation;
import net.lodoma.lime.util.HashPool32;
import net.lodoma.lime.util.Timer;
import net.lodoma.lime.world.builder.WorldFileLoader;
import net.lodoma.lime.world.server.ServersideWorld;

public class SLWorld implements ServerLogic
{
    private Server server;
    @SuppressWarnings("unused")
    private HashPool32<ServerInputHandler> sihPool;
    private HashPool32<ServerOutput> soPool;
    
    private ServersideWorld world;
    
    private Timer timer;
    
    @Override
    public void baseInit(Server server)
    {
        this.server = server;
    }
    
    @Override
    public void propertyInit()
    {
        server.setProperty("world", new ServersideWorld(server));
        server.setProperty("entityLoader", new EntityLoader());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void fetchInit()
    {
        sihPool = (HashPool32<ServerInputHandler>) server.getProperty("sihPool");
        soPool = (HashPool32<ServerOutput>) server.getProperty("soPool");
        world = (ServersideWorld) server.getProperty("world");
    }
    
    @Override
    public void generalInit()
    {
        soPool.add(SOPlatformCreation.HASH, new SOPlatformCreation(server));
        
        soPool.add(SOEntityCreation.HASH, new SOEntityCreation(server));
        soPool.add(SOEntityCorrection.HASH, new SOEntityCorrection(server));
        soPool.add(SOEntityTransformModification.HASH, new SOEntityTransformModification(server));
        soPool.add(SOEntityLinearImpulse.HASH, new SOEntityLinearImpulse(server));
        soPool.add(SOEntityAngularImpulse.HASH, new SOEntityAngularImpulse(server));
        soPool.add(SOEntityForce.HASH, new SOEntityForce(server));
        
        world.generalInit();
        
        WorldFileLoader fileLoader = new WorldFileLoader();
        fileLoader.build(world);
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
        double timeDelta = timer.getDelta();
        world.update(timeDelta);
    }
}
