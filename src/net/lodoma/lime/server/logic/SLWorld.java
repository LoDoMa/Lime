package net.lodoma.lime.server.logic;

import net.lodoma.lime.physics.entity.EntityLoader;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerInputHandler;
import net.lodoma.lime.server.ServerOutput;
import net.lodoma.lime.server.io.entity.SOEntityCorrection;
import net.lodoma.lime.server.io.entity.SOEntityCreation;
import net.lodoma.lime.server.io.entity.SOEntityLinearImpulse;
import net.lodoma.lime.server.io.entity.SOEntityTransformModification;
import net.lodoma.lime.server.io.world.SOPlatformCreation;
import net.lodoma.lime.util.HashPool;
import net.lodoma.lime.util.Timer;
import net.lodoma.lime.world.builder.WorldFileLoader;
import net.lodoma.lime.world.server.ServersideWorld;

public class SLWorld implements ServerLogic
{
    private Server server;
    @SuppressWarnings("unused")
    private HashPool<ServerInputHandler> sihPool;
    private HashPool<ServerOutput> soPool;
    
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
        sihPool = (HashPool<ServerInputHandler>) server.getProperty("sihPool");
        soPool = (HashPool<ServerOutput>) server.getProperty("soPool");
        world = (ServersideWorld) server.getProperty("world");
    }
    
    @Override
    public void generalInit()
    {
        soPool.add("Lime::PlatformCreation", new SOPlatformCreation(server, "Lime::PlatformCreation"));
        
        soPool.add("Lime::EntityCreation", new SOEntityCreation(server, "Lime::EntityCreation"));
        soPool.add("Lime::EntityCorrection", new SOEntityCorrection(server, "Lime::EntityCorrection"));
        soPool.add("Lime::EntityTransformModification", new SOEntityTransformModification(server, "Lime::EntityTransformModification"));
        soPool.add("Lime::EntityLinearImpulse", new SOEntityLinearImpulse(server, "Lime::EntityLinearImpulse"));
        
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
