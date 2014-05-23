package net.lodoma.lime.net.server;

import java.util.Set;

import net.lodoma.lime.mod.InitBundle;
import net.lodoma.lime.mod.ModDependencyException;
import net.lodoma.lime.mod.ModTarget;
import net.lodoma.lime.mod.Module;
import net.lodoma.lime.mod.ModulePool;
import net.lodoma.lime.mod.PostinitBundle;
import net.lodoma.lime.mod.PreinitBundle;
import net.lodoma.lime.mod.server.Logic;
import net.lodoma.lime.mod.server.LogicPool;
import net.lodoma.lime.net.packet.generic.ServerPacketPool;
import net.lodoma.lime.net.server.generic.GenericServer.LogLevel;
import net.lodoma.lime.net.server.generic.ServerLogic;
import net.lodoma.lime.net.server.generic.ServerUser;
import net.lodoma.lime.net.server.generic.UserPool;

public final class LimeServerLogic extends ServerLogic
{
    private boolean init;
    
    @Override
    public void onOpen()
    {
        init = false;
    }
    
    @Override
    public void onClose()
    {
        
    }
    
    @Override
    public void logic()
    {
        if (!init)
        {
            try
            {
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
            init = true;
        }
        
        ServerPacketPool packetPool = (ServerPacketPool) server.getProperty("packetPool");
        
        UserPool userPool = (UserPool) server.getProperty("userPool");
        int dependencyCount = userPool.getDependencyList().size();
        Set<ServerUser> waitingUsers = userPool.getWaitingUsers();
        for (ServerUser user : waitingUsers)
            if (user.dependencies == dependencyCount)
                packetPool.getPacket("Lime::UserStatus").send(server, user, user, userPool);
        
        LogicPool logicPool = (LogicPool) server.getProperty("logicPool");
        Set<Logic> logicComponents = logicPool.getLogicComponents();
        for (Logic logic : logicComponents)
            logic.logic();
    }
}
