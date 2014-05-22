package net.lodoma.lime.net.server;

import java.util.Set;

import net.lodoma.lime.mod.InitBundle;
import net.lodoma.lime.mod.ModTarget;
import net.lodoma.lime.mod.Module;
import net.lodoma.lime.mod.ModulePool;
import net.lodoma.lime.mod.PostinitBundle;
import net.lodoma.lime.mod.PreinitBundle;
import net.lodoma.lime.mod.server.Logic;
import net.lodoma.lime.mod.server.LogicPool;
import net.lodoma.lime.net.server.generic.GenericServer.LogLevel;
import net.lodoma.lime.net.server.generic.ServerLogic;

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
                
                PreinitBundle preinitBundle = new PreinitBundle(new String[]
                {}, new Object[]
                {});
                for (Module module : modules)
                    if (module.hasPreinit())
                        module.invokePreinit(preinitBundle);
                
                InitBundle initBundle = new InitBundle(new String[]
                { InitBundle.SERVER }, new Object[]
                { server });
                for (Module module : modules)
                    if (module.hasInit())
                        module.invokeInit(initBundle);
                
                PostinitBundle postinitBundle = new PostinitBundle(new String[]
                {}, new Object[]
                {});
                for (Module module : modules)
                    if (module.hasPostinit())
                        module.invokePostinit(postinitBundle);
            }
            catch (Exception e)
            {
                server.log(LogLevel.SEVERE, e);
            }
            init = true;
        }
        
        LogicPool logicPool = (LogicPool) server.getProperty("logicPool");
        Set<Logic> logicComponents = logicPool.getLogicComponents();
        for(Logic logic : logicComponents)
            logic.logic();
    }
}
