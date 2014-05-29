package net.lodoma.lime.client.net;

import java.util.Set;

import net.lodoma.lime.client.generic.net.ClientLogic;
import net.lodoma.lime.client.generic.net.packet.ClientPacketPool;
import net.lodoma.lime.client.net.packet.CPConnectRequest;
import net.lodoma.lime.client.net.packet.CPDependencyRequest;
import net.lodoma.lime.client.net.packet.CPHConnectRequestAnswer;
import net.lodoma.lime.client.net.packet.CPHModuleDependency;
import net.lodoma.lime.client.net.packet.CPHResponse;
import net.lodoma.lime.client.net.packet.CPHUserStatus;
import net.lodoma.lime.client.net.packet.CPResponseRequest;
import net.lodoma.lime.common.net.LogLevel;
import net.lodoma.lime.common.net.Logic;
import net.lodoma.lime.common.net.LogicPool;
import net.lodoma.lime.common.net.NetStage;
import net.lodoma.lime.mod.ModTarget;
import net.lodoma.lime.mod.Module;
import net.lodoma.lime.mod.ModulePool;
import net.lodoma.lime.mod.init.InitBundle;
import net.lodoma.lime.mod.init.PostinitBundle;
import net.lodoma.lime.mod.init.PreinitBundle;
import net.lodoma.lime.mod.targetserver.ModDependencyException;

public class LimeClientLogic extends ClientLogic
{
    private boolean checkedConnection;
    
    @Override
    public void onOpen()
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
        
        LogicPool logicPool = (LogicPool) client.getProperty("logicPool");
        Set<Logic> logicComponents = logicPool.getLogicComponents();
        for (Logic logic : logicComponents)
            logic.onOpen();
    }
    
    @Override
    public void onClose()
    {
        LogicPool logicPool = (LogicPool) client.getProperty("logicPool");
        Set<Logic> logicComponents = logicPool.getLogicComponents();
        for (Logic logic : logicComponents)
            logic.onClose();
    }
    
    @Override
    public void logic()
    {
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
