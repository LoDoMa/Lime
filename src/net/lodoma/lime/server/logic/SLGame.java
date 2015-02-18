package net.lodoma.lime.server.logic;

import java.io.IOException;

import net.lodoma.lime.Lime;
import net.lodoma.lime.script.event.EventManager;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerPacket;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.server.packet.SPSnapshot;
import net.lodoma.lime.snapshot.Snapshot;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.world.WorldSnapshot;
import net.lodoma.lime.world.WorldSnapshotSegment;
import net.lodoma.lime.world.World;
import net.lodoma.lime.world.physics.PhysicsWorld;

public class SLGame extends ServerLogic
{
    public static final String NAME = "Lime::Game";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public static final double UPDATE_PS = 33;
    public static final double UPDATE_MAXTIME = 1.0 / UPDATE_PS;
    public double updateTime = UPDATE_MAXTIME;

    public ServerPacket snapshotPacket;
    
    public SLGame(Server server)
    {
        super(server, HASH);
    }
    
    @Override
    public void init()
    {
        server.world = new World();
        server.physicsWorld = new PhysicsWorld();
        
        snapshotPacket = server.spPool.get(SPSnapshot.HASH);
        
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
    public void destroy()
    {
        server.physicsWorld.destroy();
        server.world.clean();
    }
    
    @Override
    public void update(double timeDelta)
    {
        EventManager.runEvents();
        
        updateTime -= timeDelta;
        if (updateTime <= 0.0)
        {
            server.userManager.foreach((ServerUser user) -> user.inputData.update());
            
            server.world.updateGamemode(UPDATE_MAXTIME);
            server.world.updateEntities(UPDATE_MAXTIME);
            server.physicsWorld.update((float) UPDATE_MAXTIME);
        }
        
        while (updateTime <= 0.0)
            updateTime += UPDATE_MAXTIME;
    }
    
    @Override
    public boolean acceptUser(ServerUser user)
    {
        return true;
    }
    
    @Override
    public boolean respondBroadcast()
    {
        return true;
    }
    
    @Override
    public void sendSnapshots()
    {
        if (snapshotPacket == null)
            snapshotPacket = server.spPool.get(SPSnapshot.HASH);
        
        WorldSnapshot snapshot = new WorldSnapshot(server);
        
        server.userManager.foreach((ServerUser user) -> {
            WorldSnapshot lastSnapshot = user.lastSnapshot;
            user.lastSnapshot = snapshot;
            
            if (lastSnapshot == null)
            {
                Lime.LOGGER.I("Will send full snapshot to user " + user.identifier);
                lastSnapshot = new WorldSnapshot();
            }
            
            WorldSnapshotSegment segment = new WorldSnapshotSegment(snapshot, lastSnapshot);
            snapshotPacket.write(user, new Snapshot(this, 0, segment));
        });
    }
}
