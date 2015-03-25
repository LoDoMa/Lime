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
        
        snapshotPacket = server.spPool.get(SPSnapshot.HASH);
        
        String gamemodeName = "test";
        try
        {
            server.world.load(gamemodeName, server);
        }
        catch(IOException e)
        {
            Lime.LOGGER.C("Failed to load gamemode " + gamemodeName);
            Lime.LOGGER.log(e);
            Lime.forceExit(e);
        }
        
        server.world.init();
    }
    
    @Override
    public void destroy()
    {
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
            server.world.physicsWorld.update((float) UPDATE_MAXTIME);
            server.world.postUpdateGamemode();
            server.world.postUpdateEntities();
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
        server.world.particleDefinitionList.clear();
        
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
