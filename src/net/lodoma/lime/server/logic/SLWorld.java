package net.lodoma.lime.server.logic;

import java.io.File;

import net.lodoma.lime.physics.entity.EntityLoader;
import net.lodoma.lime.physics.entity.EntityLoaderException;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerPacket;
import net.lodoma.lime.server.ServerPacketHandler;
import net.lodoma.lime.server.io.entity.SPEntityAngularImpulse;
import net.lodoma.lime.server.io.entity.SPEntityCorrection;
import net.lodoma.lime.server.io.entity.SPEntityCreation;
import net.lodoma.lime.server.io.entity.SPEntityForce;
import net.lodoma.lime.server.io.entity.SPEntityLinearImpulse;
import net.lodoma.lime.server.io.entity.SPEntityTransformModification;
import net.lodoma.lime.server.io.entity.SPSetActor;
import net.lodoma.lime.server.io.world.SPPlatformCreation;
import net.lodoma.lime.util.HashPool32;
import net.lodoma.lime.util.Timer;
import net.lodoma.lime.world.WorldLoader;
import net.lodoma.lime.world.WorldLoaderException;
import net.lodoma.lime.world.server.ServersideWorld;

public class SLWorld implements ServerLogic
{
    private Server server;
    @SuppressWarnings("unused")
    private HashPool32<ServerPacketHandler> sphPool;
    private HashPool32<ServerPacket> spPool;
    
    private ServersideWorld world;
    private EntityLoader entityLoader;
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
        server.setProperty("entityLoader", new EntityLoader());
        server.setProperty("worldLoader", new WorldLoader());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void fetchInit()
    {
        sphPool = (HashPool32<ServerPacketHandler>) server.getProperty("sphPool");
        spPool = (HashPool32<ServerPacket>) server.getProperty("spPool");
        world = (ServersideWorld) server.getProperty("world");
        entityLoader = (EntityLoader) server.getProperty("entityLoader");
        worldLoader = (WorldLoader) server.getProperty("worldLoader");
    }
    
    @Override
    public void generalInit()
    {
        spPool.add(SPPlatformCreation.HASH, new SPPlatformCreation(server));
        
        spPool.add(SPEntityCreation.HASH, new SPEntityCreation(server));
        spPool.add(SPEntityCorrection.HASH, new SPEntityCorrection(server));
        spPool.add(SPEntityTransformModification.HASH, new SPEntityTransformModification(server));
        spPool.add(SPEntityLinearImpulse.HASH, new SPEntityLinearImpulse(server));
        spPool.add(SPEntityAngularImpulse.HASH, new SPEntityAngularImpulse(server));
        spPool.add(SPEntityForce.HASH, new SPEntityForce(server));
        
        spPool.add(SPSetActor.HASH, new SPSetActor(server));
        
        world.generalInit();

        try
        {
            entityLoader.addAllFiles(world, world.getServer());
        }
        catch(EntityLoaderException e)
        {
            // TODO: handle later
            e.printStackTrace();
        }
        
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
