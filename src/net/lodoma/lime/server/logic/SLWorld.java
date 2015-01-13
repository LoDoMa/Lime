package net.lodoma.lime.server.logic;

import java.io.IOException;

import net.lodoma.lime.script.event.EventManager;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.server.packet.SPHInputState;
import net.lodoma.lime.server.packet.SPSnapshot;
import net.lodoma.lime.util.Timer;
import net.lodoma.lime.world.SnapshotManager;
import net.lodoma.lime.world.World;
import net.lodoma.lime.world.entity.physics.PhysicsWorld;

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
        server.physicsWorld = new PhysicsWorld();
        server.snapshotManager = new SnapshotManager(server.world, server.userManager);
        
        server.spPool.add(new SPSnapshot(server));
        server.sphPool.add(new SPHInputState(server));
        
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
        
        server.world.init();
        server.physicsWorld.create();
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
        
        server.userManager.foreach((ServerUser user) -> {
            user.inputData.update();
        });
        
        updateTime -= timeDelta;
        if (updateTime <= 0.0)
        {
            server.world.updateGamemode(UPDATE_MAXTIME);
            server.world.updateEntities(UPDATE_MAXTIME);
            server.physicsWorld.update((float) UPDATE_MAXTIME);
            EventManager.runEvents();
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
