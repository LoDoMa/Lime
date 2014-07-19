package net.lodoma.lime.server.logic;

import java.io.File;

import net.lodoma.lime.physics.entity.Entity;
import net.lodoma.lime.physics.entity.EntityLoader;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerInputHandler;
import net.lodoma.lime.server.ServerOutput;
import net.lodoma.lime.server.io.world.SIHInitialWorldRequest;
import net.lodoma.lime.server.io.world.SOInitialWorldData;
import net.lodoma.lime.util.HashPool;
import net.lodoma.lime.util.Timer;
import net.lodoma.lime.world.builder.WorldFileLoader;
import net.lodoma.lime.world.server.ServersideWorld;

public class SLWorld implements ServerLogic
{
    private Server server;
    private HashPool<ServerInputHandler> sihPool;
    private HashPool<ServerOutput> soPool;
    private ServersideWorld world;
    private EntityLoader entityLoader;
    
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
        entityLoader = (EntityLoader) server.getProperty("entityLoader");
        
        world.fetch();
    }
    
    @Override
    public void generalInit()
    {
        sihPool.add("Lime::InitialWorldRequest", new SIHInitialWorldRequest(server));
        soPool.add("Lime::InitialWorldData", new SOInitialWorldData(server, "Lime::InitialWorldData"));
        
        WorldFileLoader fileLoader = new WorldFileLoader();
        fileLoader.build(world);
        
        try
        {
            Entity entity = entityLoader.loadFromXML(new File("model/zombie.xml"), world);
            world.createEntity(entity.getID());
            world.addEntity(entity);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
    }
}
