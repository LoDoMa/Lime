package net.lodoma.lime.server;

import java.util.HashMap;
import java.util.Map;

import net.lodoma.lime.common.PropertyPool;
import net.lodoma.lime.event.EventManager;
import net.lodoma.lime.server.dependency.DependencyPool;
import net.lodoma.lime.server.logic.SLBase;
import net.lodoma.lime.server.logic.SLChat;
import net.lodoma.lime.server.logic.SLWorld;
import net.lodoma.lime.server.logic.ServerLogicPool;
import net.lodoma.lime.server.logic.UserManager;
import net.lodoma.lime.util.HashPool32;

public final class Server implements PropertyPool
{
    private boolean isRunning = false;
    private String closeMessage;
    
    private ServerService service;
    
    private ServerLogicPool logicPool;
    private Map<String, Object> properties;
    
    public void open(int port)
    {
        if (isRunning) return;
        
        logicPool = new ServerLogicPool(this, 60.0f);
        properties = new HashMap<String, Object>();
        
        UserManager userManager = new UserManager();
        
        setProperty("sphPool", new HashPool32<ServerPacketHandler>());
        setProperty("spPool", new HashPool32<ServerPacket>());
        setProperty("emanPool", new HashPool32<EventManager>());
        setProperty("dependencyPool", new DependencyPool());
        setProperty("userManager", userManager);
        
        logicPool.addLogic(new SLBase());
        logicPool.addLogic(new SLChat());
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

    @Override
    public Object getProperty(String name)
    {
        return properties.get(name);
    }

    @Override
    public void setProperty(String name, Object value)
    {
        properties.put(name, value);
    }

    @Override
    public void removeProperty(String name)
    {
        properties.remove(name);
    }

    @Override
    public boolean hasProperty(String name)
    {
        return properties.containsKey(name);
    }
}
