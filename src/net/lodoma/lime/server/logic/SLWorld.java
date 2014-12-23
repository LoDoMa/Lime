package net.lodoma.lime.server.logic;

import java.io.IOException;

import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerPacket;
import net.lodoma.lime.server.packet.SPSnapshot;
import net.lodoma.lime.util.HashPool32;
import net.lodoma.lime.util.Timer;
import net.lodoma.lime.world.SnapshotManager;
import net.lodoma.lime.world.World;
import net.lodoma.lime.world.entity.physics.PhysicsEngine;

public class SLWorld implements ServerLogic
{
    public static final double UPDATE_PS = 33;
    public static final double UPDATE_MAXTIME = 1.0 / UPDATE_PS;
    public double updateTime = UPDATE_MAXTIME;
    
    public static final double SNAPSHOT_PS = 20;
    public static final double SNAPSHOT_MAXTIME = 1.0 / SNAPSHOT_PS;
    public double snapshotTime = SNAPSHOT_MAXTIME;
    
    private Server server;
    private HashPool32<ServerPacket> spPool;
    
    private World world;
    private PhysicsEngine physics;
    private SnapshotManager snapshotManager;
    
    private Timer timer;
    
    @Override
    public void baseInit(Server server)
    {
        this.server = server;
    }
    
    @Override
    public void propertyInit()
    {
        World world = new World();
        server.setProperty("world", world);
        server.setProperty("physicsEngine", new PhysicsEngine(world));
        server.setProperty("snapshotManager", new SnapshotManager(world));
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void fetchInit()
    {
        spPool = (HashPool32<ServerPacket>) server.getProperty("spPool");
        
        world = (World) server.getProperty("world");
        physics = (PhysicsEngine) server.getProperty("physicsEngine");
        snapshotManager = (SnapshotManager) server.getProperty("snapshotManager");
    }
    
    @Override
    public void generalInit()
    {
        spPool.add(SPSnapshot.HASH, new SPSnapshot(server));
        
        snapshotManager.userManager = (UserManager) server.getProperty("userManager");
        snapshotManager.snapshotPacket = spPool.get(SPSnapshot.HASH);
        
        try
        {
            world.load("test");
        }
        catch(IOException e)
        {
            // TODO: handle me!
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
        
        updateTime -= timeDelta;
        if (updateTime <= 0.0)
        {
            physics.update(timeDelta);
            world.updateGamemode(timeDelta);
        }
        while (updateTime <= 0.0)
            updateTime += UPDATE_MAXTIME;
        
        snapshotTime -= timeDelta;
        if (snapshotTime <= 0.0)
        {
            snapshotManager.send();
        }
        while (snapshotTime <= 0.0)
            snapshotTime += SNAPSHOT_MAXTIME;
    }
}
