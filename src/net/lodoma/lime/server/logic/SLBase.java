package net.lodoma.lime.server.logic;

import net.lodoma.lime.event.EventManager;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerInputHandler;
import net.lodoma.lime.server.ServerOutput;
import net.lodoma.lime.server.dependency.DependencyPool;
import net.lodoma.lime.server.io.base.SIHDependencyRequest;
import net.lodoma.lime.server.io.base.SOModificationCheck;
import net.lodoma.lime.server.io.base.SONetworkStageChange;
import net.lodoma.lime.util.HashPool32;

public class SLBase implements ServerLogic
{
    private Server server;
    private HashPool32<ServerInputHandler> sihPool;
    private HashPool32<ServerOutput> soPool;
    private HashPool32<EventManager> emanPool;
    private DependencyPool dependencyPool;
    
    @Override
    public void baseInit(Server server)
    {
        this.server = server;
    }
    
    @Override
    public void propertyInit()
    {
        
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void fetchInit()
    {
        sihPool = (HashPool32<ServerInputHandler>) server.getProperty("sihPool");
        soPool = (HashPool32<ServerOutput>) server.getProperty("soPool");
        emanPool = (HashPool32<EventManager>) server.getProperty("emanPool");
        dependencyPool = (DependencyPool) server.getProperty("dependencyPool");
        
        emanPool.add(EventManager.ON_NEW_USER_HASH, new EventManager());
    }
    
    @Override
    public void generalInit()
    {
        sihPool.add(SIHDependencyRequest.HASH, new SIHDependencyRequest(server));
        soPool.add(SONetworkStageChange.HASH, new SONetworkStageChange(server));
        
        dependencyPool.addDependency(new SOModificationCheck(server));
    }
    
    @Override
    public void clean()
    {
        
    }
    
    @Override
    public void logic()
    {
        
    }
}
