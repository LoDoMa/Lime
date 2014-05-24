package net.lodoma.lime.net.client;

import java.util.Set;

import net.lodoma.lime.mod.InitBundle;
import net.lodoma.lime.mod.ModDependencyException;
import net.lodoma.lime.mod.ModTarget;
import net.lodoma.lime.mod.Module;
import net.lodoma.lime.mod.ModulePool;
import net.lodoma.lime.mod.PostinitBundle;
import net.lodoma.lime.mod.PreinitBundle;
import net.lodoma.lime.net.LogLevel;
import net.lodoma.lime.net.Logic;
import net.lodoma.lime.net.LogicPool;
import net.lodoma.lime.net.NetStage;
import net.lodoma.lime.net.client.generic.ClientLogic;
import net.lodoma.lime.net.packet.CPConnectRequest;
import net.lodoma.lime.net.packet.CPDependencyRequest;
import net.lodoma.lime.net.packet.CPHConnectRequestAnswer;
import net.lodoma.lime.net.packet.check.CPHResponse;
import net.lodoma.lime.net.packet.check.CPResponseRequest;
import net.lodoma.lime.net.packet.dependency.CPHModuleDependency;
import net.lodoma.lime.net.packet.dependency.CPHUserStatus;
import net.lodoma.lime.net.packet.generic.ClientPacketPool;

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
                ClientPacketPool packetPool = (ClientPacketPool) client.getProperty("packetPool");
                packetPool.addPacket("Lime::ConnectRequest", new CPConnectRequest());
                packetPool.addHandler("Lime::ConnectRequestAnswer", new CPHConnectRequestAnswer());
                packetPool.addPacket("Lime::DependencyRequest", new CPDependencyRequest());
                packetPool.addHandler("Lime::UserStatus", new CPHUserStatus());
                packetPool.addHandler("Lime::ModuleDependency", new CPHModuleDependency());
                packetPool.addPacket("Lime::ResponseRequest", new CPResponseRequest());
                packetPool.addHandler("Lime::Response", new CPHResponse());
                
                ModulePool modulePool = new ModulePool();
                client.setProperty("modulePool", modulePool);
                
                LogicPool logicPool = new LogicPool();
                client.setProperty("logicPool", logicPool);
                
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
                
                client.setProperty("stage", NetStage.PRIMITIVE);
                packetPool.getPacket("Lime::ConnectRequest").send(client);
                
                client.setProperty("lastServerResponse", System.currentTimeMillis());
            }
            catch (Exception e)
            {
                client.log(LogLevel.SEVERE, e);
            }
            init = true;
        }
        
        if (client.hasProperty("lastServerResponse"))
        {
            long currentTime = System.currentTimeMillis();
            long lastTime = (Long) client.getProperty("lastServerResponse");
            long timeDelta = currentTime - lastTime;
            if (timeDelta >= 1000)
            {
                if (!checkedConnection)
                {
                    ((ClientPacketPool) client.getProperty("packetPool")).getPacket("Lime::ResponseRequest").send(client);;
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
        
        LogicPool logicPool = (LogicPool) client.getProperty("logicPool");
        Set<Logic> logicComponents = logicPool.getLogicComponents();
        for (Logic logic : logicComponents)
            logic.logic();
    }
}
