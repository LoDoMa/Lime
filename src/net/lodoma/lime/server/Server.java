package net.lodoma.lime.server;

import net.lodoma.lime.script.event.EventManager;
import net.lodoma.lime.server.logic.SLWorld;
import net.lodoma.lime.server.logic.ServerLogicPool;
import net.lodoma.lime.server.logic.UserManager;
import net.lodoma.lime.util.IdentityPool;
import net.lodoma.lime.world.SnapshotManager;
import net.lodoma.lime.world.World;
import net.lodoma.lime.world.physics.PhysicsWorld;

public final class Server
{
    private boolean isRunning = false;
    private String closeMessage;

    private ServerService service;
    private ServerBroadcastService broadcast;
    
    public ServerLogicPool logicPool;
    
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
        
        logicPool = new ServerLogicPool(this);
        
        userManager = new UserManager();
        sphPool = new IdentityPool<ServerPacketHandler>(true);
        spPool = new IdentityPool<ServerPacket>(true);
        emanPool = new IdentityPool<EventManager>(true);
        
        logicPool.addLogic(new SLWorld());
        logicPool.addLogic(userManager);
        
        logicPool.init();
        
        isRunning = true;
        
        service = new ServerService(this);
        service.start();
        
        broadcast = new ServerBroadcastService(this);
        broadcast.start();
        
        logicPool.start();
    }
    
    public void close()
    {
        if (!isRunning) return;
        
        if(closeMessage != null)
            System.err.println("close message: " + closeMessage);
        
        logicPool.stop();
        broadcast.stop();
        service.stop();
        
        isRunning = false;
        
        try
        {
            while(logicPool.isRunning()) Thread.sleep(1);
            while(service.isRunning()) Thread.sleep(1);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }
     
    public void closeInThread()
    {
        if(!isRunning) return;
        
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                close();
            }
        }, "ServerCloseThread").start();
    }
    
    public String getCloseMessage()
    {
        return closeMessage;
    }
    
    public void setCloseMessage(String closeMessage)
    {
        this.closeMessage = closeMessage;
    }
}
