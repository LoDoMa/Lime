package net.lodoma.lime.server;

import net.lodoma.lime.event.EventManager;
import net.lodoma.lime.server.logic.SLWorld;
import net.lodoma.lime.server.logic.ServerLogicPool;
import net.lodoma.lime.server.logic.UserManager;
import net.lodoma.lime.util.HashPool32;
import net.lodoma.lime.world.SnapshotManager;
import net.lodoma.lime.world.World;
import net.lodoma.lime.world.entity.physics.PhysicsEngine;

public final class Server
{
    private boolean isRunning = false;
    private String closeMessage;
    
    private ServerService service;
    
    public ServerLogicPool logicPool;
    
    public UserManager userManager;
    public HashPool32<ServerPacket> spPool;
    public HashPool32<ServerPacketHandler> sphPool;
    public HashPool32<EventManager> emanPool;
    
    public World world;
    public PhysicsEngine physicsEngine;
    public SnapshotManager snapshotManager;
    
    public void open(int port)
    {
        if (isRunning) return;
        
        logicPool = new ServerLogicPool(this);
        
        userManager = new UserManager();
        sphPool = new HashPool32<ServerPacketHandler>();
        spPool = new HashPool32<ServerPacket>();
        emanPool = new HashPool32<EventManager>();
        
        logicPool.addLogic(new SLWorld());
        logicPool.addLogic(userManager);
        
        logicPool.init();
        
        isRunning = true;
        
        service = new ServerService(this);
        service.setPort(port);
        service.start();
        
        logicPool.start();
    }
    
    public void close()
    {
        if (!isRunning) return;
        
        if(closeMessage != null)
            System.err.println("close message: " + closeMessage);
        
        logicPool.stop();
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
