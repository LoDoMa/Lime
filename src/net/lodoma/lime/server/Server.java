package net.lodoma.lime.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.lodoma.lime.server.dependency.DependencyPool;
import net.lodoma.lime.server.logic.SLBase;
import net.lodoma.lime.server.logic.SLChat;
import net.lodoma.lime.server.logic.SLWorld;
import net.lodoma.lime.server.logic.ServerLogicPool;
import net.lodoma.lime.util.HashPool;

public class Server
{
    private boolean isRunning = false;
    
    private ServerService service;
    
    private ServerLogicPool logicPool;
    private  Map<String, Object> properties;
    
    public final void open(int port)
    {
        if (isRunning)
            throw new IllegalStateException("server is already open");
        
        logicPool = new ServerLogicPool(this);
        properties = new HashMap<String, Object>();
        
        setProperty("userManager", new UserManager());
        setProperty("sihPool", new HashPool<ServerInputHandler>());
        setProperty("soPool", new HashPool<ServerOutput>());
        setProperty("dependencyPool", new DependencyPool());

        logicPool.addLogic(new SLBase());
        logicPool.addLogic(new SLChat());
        logicPool.addLogic(new SLWorld());
        
        logicPool.init();
        
        service = new ServerService(this);
        service.setPort(port);
        service.start();
        
        isRunning = true;
        
        logicPool.start();
    }
    
    public final void close()
    {
        if (!isRunning)
            new IllegalStateException("server is already closed");

        UserManager manager = (UserManager) getProperty("userManager");
        List<ServerUser> serverUsers = manager.getUserList();
        for(ServerUser serverUser : serverUsers)
            serverUser.stop();
        
        logicPool.stop();
        service.stop();
        
        isRunning = false;
    }
    
    public final Object getProperty(String name)
    {
        return properties.get(name);
    }
    
    public final void setProperty(String name, Object value)
    {
        properties.put(name, value);
    }
    
    public final void removeProperty(String name)
    {
        properties.remove(name);
    }
    
    public final boolean hasProperty(String name)
    {
        return properties.containsKey(name);
    }
}