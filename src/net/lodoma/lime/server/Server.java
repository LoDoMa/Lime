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
import net.lodoma.lime.util.HashPool;
import net.lodoma.lime.util.HashPool32;

public class Server implements PropertyPool
{
    private boolean isRunning = false;
    
    private ServerService service;
    
    private ServerLogicPool logicPool;
    private Map<String, Object> properties;
    
    public final void open(int port)
    {
        if (isRunning) return;
        
        logicPool = new ServerLogicPool(this, 60.0f);
        properties = new HashMap<String, Object>();
        
        UserManager userManager = new UserManager();
        
        setProperty("sihPool", new HashPool<ServerInputHandler>());
        setProperty("soPool", new HashPool<ServerOutput>());
        setProperty("emanPool", new HashPool32<EventManager>());
        setProperty("dependencyPool", new DependencyPool());
        setProperty("userManager", userManager);
        
        logicPool.addLogic(new SLBase());
        logicPool.addLogic(new SLChat());
        logicPool.addLogic(new SLWorld());
        logicPool.addLogic(userManager);
        
        logicPool.init();
        
        service = new ServerService(this);
        service.setPort(port);
        service.start();
        
        isRunning = true;
        
        logicPool.start();
    }
    
    public final void close()
    {
        if (!isRunning) return;
        
        logicPool.stop();
        service.stop();
        
        isRunning = false;
    }

    @Override
    public final Object getProperty(String name)
    {
        return properties.get(name);
    }

    @Override
    public final void setProperty(String name, Object value)
    {
        properties.put(name, value);
    }

    @Override
    public final void removeProperty(String name)
    {
        properties.remove(name);
    }

    @Override
    public final boolean hasProperty(String name)
    {
        return properties.containsKey(name);
    }
}
