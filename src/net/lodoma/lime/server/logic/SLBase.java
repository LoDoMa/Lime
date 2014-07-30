package net.lodoma.lime.server.logic;

import net.lodoma.lime.event.EventManager;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerPacketHandler;
import net.lodoma.lime.server.ServerPacket;
import net.lodoma.lime.server.dependency.DependencyPool;
import net.lodoma.lime.server.io.base.SPHDependencyRequest;
import net.lodoma.lime.server.io.base.SPModificationCheck;
import net.lodoma.lime.server.io.base.SPNetworkStageChange;
import net.lodoma.lime.util.HashPool32;

public class SLBase implements ServerLogic
{
    private Server server;
    private HashPool32<ServerPacketHandler> sphPool;
    private HashPool32<ServerPacket> spPool;
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
        sphPool = (HashPool32<ServerPacketHandler>) server.getProperty("sphPool");
        spPool = (HashPool32<ServerPacket>) server.getProperty("spPool");
        emanPool = (HashPool32<EventManager>) server.getProperty("emanPool");
        dependencyPool = (DependencyPool) server.getProperty("dependencyPool");
        
        emanPool.add(EventManager.ON_NEW_USER_HASH, new EventManager());
    }
    
    @Override
    public void generalInit()
    {
        sphPool.add(SPHDependencyRequest.HASH, new SPHDependencyRequest(server));
        spPool.add(SPNetworkStageChange.HASH, new SPNetworkStageChange(server));
        
        dependencyPool.addDependency(new SPModificationCheck(server));
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
