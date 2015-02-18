package net.lodoma.lime.server;

import net.lodoma.lime.script.event.EventManager;
import net.lodoma.lime.server.logic.SLGame;
import net.lodoma.lime.server.logic.ServerLogic;
import net.lodoma.lime.server.logic.ServerLogicThread;
import net.lodoma.lime.util.IdentityPool;
import net.lodoma.lime.world.SnapshotManager;
import net.lodoma.lime.world.World;
import net.lodoma.lime.world.physics.PhysicsWorld;

public final class Server
{
    private boolean isRunning = false;

    private ServerService service;
    private ServerBroadcastService broadcast;
    
    public ServerLogic logic;
    public ServerLogicThread logicThread;
    
    public UserManager userManager;
    public IdentityPool<ServerPacket> spPool;
    public IdentityPool<ServerPacketHandler> sphPool;
    public IdentityPool<EventManager> emanPool;
    
    public World world;
    public PhysicsWorld physicsWorld;
    public SnapshotManager snapshotManager;
    
    public void open()
    {
        if (isRunning) return;
        
        userManager = new UserManager();
        sphPool = new IdentityPool<ServerPacketHandler>(true);
        spPool = new IdentityPool<ServerPacket>(true);
        emanPool = new IdentityPool<EventManager>(true);
        
        isRunning = true;
        
        service = new ServerService(this);
        service.start();
        
        broadcast = new ServerBroadcastService(this);
        broadcast.start();

        logicThread = new ServerLogicThread(this, 60);
        logicThread.start();
        
        logic = new SLGame(this);
    }
    
    public void close()
    {
        if (!isRunning) return;
        
        logicThread.stop();
        broadcast.stop();
        service.stop();
        
        isRunning = false;
        
        try
        {
            while(logicThread.isRunning()) Thread.sleep(1);
            while(broadcast.isRunning()) Thread.sleep(1);
            while(service.isRunning()) Thread.sleep(1);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
