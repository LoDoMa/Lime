package net.lodoma.lime.server.logic;

import java.io.IOException;

import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.packet.SPSnapshot;
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
    
    private Timer timer;
    
    @Override
    public void init(Server server)
    {
        this.server = server;
        server.world = new World();
        server.physicsEngine = new PhysicsEngine(server.world);
        server.snapshotManager = new SnapshotManager(server.world, server.userManager);
        
        server.spPool.add(new SPSnapshot(server));
        server.snapshotManager.snapshotPacket = server.spPool.get(SPSnapshot.HASH);
        
        try
        {
            server.world.load("test", server);
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
        server.world.clean();
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
            server.world.updateGamemode(UPDATE_MAXTIME);
            server.physicsEngine.update(UPDATE_MAXTIME);
        }
        
        while (updateTime <= 0.0)
            updateTime += UPDATE_MAXTIME;
        
        snapshotTime -= timeDelta;
        if (snapshotTime <= 0.0)
        {
            server.snapshotManager.send();
        }
        while (snapshotTime <= 0.0)
            snapshotTime += SNAPSHOT_MAXTIME;
    }
}
