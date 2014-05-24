package net.lodoma.lime.net.client;

import java.util.Set;

import net.lodoma.lime.mod.InitBundle;
import net.lodoma.lime.mod.ModDependencyException;
import net.lodoma.lime.mod.ModTarget;
import net.lodoma.lime.mod.Module;
import net.lodoma.lime.mod.ModulePool;
import net.lodoma.lime.mod.PostinitBundle;
import net.lodoma.lime.mod.PreinitBundle;
import net.lodoma.lime.net.client.generic.ClientLogic;
import net.lodoma.lime.util.LogLevel;

public class LimeClientLogic extends ClientLogic
{
    private boolean init;
    private boolean checkedConnection;
    
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
                ModulePool modulePool = new ModulePool();
                client.setProperty("modulePool", modulePool);
                
                modulePool.loadModules(ModTarget.CLIENTSIDE);
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
                        { InitBundle.CLIENT, InitBundle.MODULE }, new Object[]
                        { client, module }));
                        Set<String> dependencies = module.getClientModuleDependencies();
                        for (String dependency : dependencies)
                            if (!modulePool.isModuleLoaded(dependency))
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
                client.log(LogLevel.SEVERE, e);
            }
            init = true;
        }
        
        if (client.hasProperty("lastConnectionTime"))
        {
            long currentTime = System.currentTimeMillis();
            long lastTime = (Long) client.getProperty("lastConnectionTime");
            long timeDelta = currentTime - lastTime;
            if (timeDelta >= 1000)
            {
                if (!checkedConnection)
                {
                    // TODO: add connection check
                    checkedConnection = true;
                }
                if (timeDelta >= 2000)
                {
                    System.out.println("server not responding");
                }
            }
            else
                checkedConnection = false;
        }
    }
}
