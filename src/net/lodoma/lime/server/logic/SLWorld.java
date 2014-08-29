package net.lodoma.lime.server.logic;

import java.io.File;
import java.util.HashMap;

import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerPacket;
import net.lodoma.lime.server.io.entity.SPEntityCorrection;
import net.lodoma.lime.server.io.entity.SPEntityCreation;
import net.lodoma.lime.server.io.entity.SPSetActor;
import net.lodoma.lime.util.HashPool32;
import net.lodoma.lime.util.Timer;
import net.lodoma.lime.world.WorldLoader;
import net.lodoma.lime.world.WorldLoaderException;
import net.lodoma.lime.world.server.ServersideWorld;

public class SLWorld implements ServerLogic
{
    private Server server;
    private HashPool32<ServerPacket> spPool;
    
    private ServersideWorld world;
    private WorldLoader worldLoader;
    
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
        server.setProperty("worldLoader", new WorldLoader());
        server.setProperty("actors", new HashMap<Integer, Integer>());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void fetchInit()
    {
        spPool = (HashPool32<ServerPacket>) server.getProperty("spPool");
        world = (ServersideWorld) server.getProperty("world");
        worldLoader = (WorldLoader) server.getProperty("worldLoader");
    }
    
    @Override
    public void generalInit()
    {
        spPool.add(SPEntityCreation.HASH, new SPEntityCreation(server));
        spPool.add(SPEntityCorrection.HASH, new SPEntityCorrection(server));
        
        spPool.add(SPSetActor.HASH, new SPSetActor(server));
        
        world.load();
        world.generalInit();
        
        try
        {
            worldLoader.loadFromXML(new File("world/test.xml"), world);
        }
        catch(WorldLoaderException e)
        {
            // TODO: handle later
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
        double timeDelta = timer.getDelta();
        world.update(timeDelta);
    }
}
