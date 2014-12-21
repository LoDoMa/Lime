package net.lodoma.lime.server.logic;

import java.io.IOException;

import net.lodoma.lime.server.Server;
import net.lodoma.lime.util.Timer;
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

    private World world;
    private PhysicsEngine physics;
    
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
    }
    
    @Override
    public void fetchInit()
    {
        world = (World) server.getProperty("world");
        physics = (PhysicsEngine) server.getProperty("physicsEngine");
    }
    
    @Override
    public void generalInit()
    {
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
            
        }
        while (snapshotTime <= 0.0)
            snapshotTime += SNAPSHOT_MAXTIME;
    }
}
