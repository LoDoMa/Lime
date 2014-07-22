package net.lodoma.lime.server.logic;

import net.lodoma.lime.event.EventManager;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerInputHandler;
import net.lodoma.lime.server.ServerOutput;
import net.lodoma.lime.server.dependency.DependencyPool;
import net.lodoma.lime.server.io.base.SIHDependencyRequest;
import net.lodoma.lime.server.io.base.SOModificationCheck;
import net.lodoma.lime.server.io.base.SONetworkStageChange;
import net.lodoma.lime.util.HashPool;

public class SLBase implements ServerLogic
{
    private Server server;
    private HashPool<ServerInputHandler> sihPool;
    private HashPool<ServerOutput> soPool;
    private HashPool<EventManager> emanPool;
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
        sihPool = (HashPool<ServerInputHandler>) server.getProperty("sihPool");
        soPool = (HashPool<ServerOutput>) server.getProperty("soPool");
        emanPool = (HashPool<EventManager>) server.getProperty("emanPool");
        dependencyPool = (DependencyPool) server.getProperty("dependencyPool");
        
        emanPool.add("Lime::onNewUser", new EventManager());
    }
    
    @Override
    public void generalInit()
    {
        sihPool.add("Lime::DependencyRequest", new SIHDependencyRequest(server));
        soPool.add("Lime::NetworkStageChange", new SONetworkStageChange(server, "Lime::NetworkStageChange"));
        dependencyPool.addDependency(new SOModificationCheck(server, "Lime::ModificationCheck"));
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
