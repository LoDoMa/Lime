package net.lodoma.lime.server.logic;

import java.io.File;

import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerInputHandler;
import net.lodoma.lime.server.ServerOutput;
import net.lodoma.lime.server.io.world.SIHInitialWorldRequest;
import net.lodoma.lime.server.io.world.SOInitialWorldData;
import net.lodoma.lime.util.HashPool;
import net.lodoma.lime.world.builder.WorldFileLoader;
import net.lodoma.lime.world.entity.EntityLoader;
import net.lodoma.lime.world.server.ServersideWorld;

public class SLWorld implements ServerLogic
{
    private Server server;
    private HashPool<ServerInputHandler> sihPool;
    private HashPool<ServerOutput> soPool;
    private ServersideWorld world;
    private EntityLoader entityLoader;
    
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
            entityLoader.loadFromXML(new File("models/zombie.xml"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void clean()
    {
        
    }
    
    @Override
    public void logic()
    {
        
    }
}
