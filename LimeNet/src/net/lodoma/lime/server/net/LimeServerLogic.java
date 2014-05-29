package net.lodoma.lime.server.net;

import java.util.Set;

import net.lodoma.lime.common.net.LogLevel;
import net.lodoma.lime.common.net.Logic;
import net.lodoma.lime.common.net.LogicPool;
import net.lodoma.lime.common.net.packet.DependencyPool;
import net.lodoma.lime.mod.ModTarget;
import net.lodoma.lime.mod.Module;
import net.lodoma.lime.mod.ModulePool;
import net.lodoma.lime.mod.init.InitBundle;
import net.lodoma.lime.mod.init.PostinitBundle;
import net.lodoma.lime.mod.init.PreinitBundle;
import net.lodoma.lime.mod.targetserver.ModDependencyException;
import net.lodoma.lime.server.generic.ServerLogic;
import net.lodoma.lime.server.generic.net.packet.ServerPacketPool;
import net.lodoma.lime.server.net.packet.SPConnectRequestAnswer;
import net.lodoma.lime.server.net.packet.SPHConnectRequest;
import net.lodoma.lime.server.net.packet.SPHDependencyRequest;
import net.lodoma.lime.server.net.packet.SPHResponseRequest;
import net.lodoma.lime.server.net.packet.SPModuleDependency;
import net.lodoma.lime.server.net.packet.SPResponse;
import net.lodoma.lime.server.net.packet.SPUserStatus;

public final class LimeServerLogic extends ServerLogic
{
    @Override
    public void onOpen()
    {
        try
        {
            ServerPacketPool packetPool = (ServerPacketPool) server.getProperty("packetPool");
            packetPool.addHandler("Lime::ConnectRequest", new SPHConnectRequest());
            packetPool.addPacket("Lime::ConnectRequestAnswer", new SPConnectRequestAnswer());
            packetPool.addHandler("Lime::DependencyRequest", new SPHDependencyRequest());
            packetPool.addPacket("Lime::UserStatus", new SPUserStatus());
            packetPool.addHandler("Lime::ResponseRequest", new SPHResponseRequest());
            packetPool.addPacket("Lime::Response", new SPResponse());
            
            DependencyPool dependencyPool = new DependencyPool();
            server.setProperty("dependencyPool", dependencyPool);
            
            LogicPool logicPool = new LogicPool();
            server.setProperty("logicPool", logicPool);
            
            ModulePool modulePool = new ModulePool();
            server.setProperty("modulePool", modulePool);
            
            modulePool.loadModules(ModTarget.SERVERSIDE);
            Set<Module> modules = modulePool.getModules();
            
            for (Module module : modules)
                if (module.hasPreinit())
                    module.invokePreinit(new PreinitBundle(new String[]
                    {}, new Object[]
                    {}));
            
            for (Module module : modules)
                if (module.hasInit())
                {
                    module.invokeInit(new InitBundle(new String[]
                    { InitBundle.SERVER, InitBundle.MODULE }, new Object[]
                    { server, module }));
                    Set<String> dependencies = module.getServerModuleDependencies();
                    for(String dependency : dependencies)
                        if(!modulePool.isModuleLoaded(dependency))
                            throw new ModDependencyException();
                    Set<String> clientDependencies = module.getClientModuleDependencies();
                    for(String dependency : clientDependencies)
                        dependencyPool.addDependency(new SPModuleDependency(dependency));
                }
            
            for (Module module : modules)
                if (module.hasPostinit())
                    module.invokePostinit(new PostinitBundle(new String[]
                    {}, new Object[]
                    {}));
        }
        catch (Exception e)
        {
            server.log(LogLevel.SEVERE, e);
        }
        
        LogicPool logicPool = (LogicPool) server.getProperty("logicPool");
        Set<Logic> logicComponents = logicPool.getLogicComponents();
        for (Logic logic : logicComponents)
            logic.onOpen();
    }
    
    @Override
    public void onClose()
    {
        LogicPool logicPool = (LogicPool) server.getProperty("logicPool");
        Set<Logic> logicComponents = logicPool.getLogicComponents();
        for (Logic logic : logicComponents)
            logic.onClose();
    }
    
    @Override
    public void logic()
    {
        LogicPool logicPool = (LogicPool) server.getProperty("logicPool");
        Set<Logic> logicComponents = logicPool.getLogicComponents();
        for (Logic logic : logicComponents)
            logic.logic();
    }
}
