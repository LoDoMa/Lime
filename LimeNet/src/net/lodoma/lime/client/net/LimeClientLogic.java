package net.lodoma.lime.client.net;

import java.util.Set;

import net.lodoma.lime.client.ClientData;
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
            ClientData data = client.getData();
            
            ClientPacketPool packetPool = data.packetPool;
            packetPool.addPacket("Lime::ConnectRequest", new CPConnectRequest());
            packetPool.addHandler("Lime::ConnectRequestAnswer", new CPHConnectRequestAnswer());
            packetPool.addPacket("Lime::DependencyRequest", new CPDependencyRequest());
            packetPool.addHandler("Lime::UserStatus", new CPHUserStatus());
            packetPool.addHandler("Lime::ModuleDependency", new CPHModuleDependency());
            packetPool.addPacket("Lime::ResponseRequest", new CPResponseRequest());
            packetPool.addHandler("Lime::Response", new CPHResponse());
            
            LogicPool logicPool = new LogicPool();
            data.logicPool = logicPool;
            
            ModulePool modulePool = new ModulePool();
            data.modulePool = modulePool;
            
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
            
            data.networkStage = NetStage.PRIMITIVE;
            packetPool.getPacket("Lime::ConnectRequest").send(client);
            
            data.lastServerResponse = System.currentTimeMillis();
            
            Set<Logic> logicComponents = logicPool.getLogicComponents();
            for (Logic logic : logicComponents)
                logic.onOpen();
        }
        catch (Exception e)
        {
            client.log(LogLevel.SEVERE, e);
        }
    }
    
    @Override
    public void onClose()
    {
        LogicPool logicPool = client.getData().logicPool;
        Set<Logic> logicComponents = logicPool.getLogicComponents();
        for (Logic logic : logicComponents)
            logic.onClose();
    }
    
    @Override
    public void logic()
    {
        ClientData data = client.getData();
        
        long currentTime = System.currentTimeMillis();
        long lastTime = data.lastServerResponse;
        long timeDelta = currentTime - lastTime;
        if (timeDelta >= 1000)
        {
            if (!checkedConnection)
            {
                data.packetPool.getPacket("Lime::ResponseRequest").send(client);;
                checkedConnection = true;
            }
            if (timeDelta >= 2000)
            {
                System.out.println("server not responding");
            }
        }
        else
            checkedConnection = false;
        
        Set<Logic> logicComponents = data.logicPool.getLogicComponents();
        for (Logic logic : logicComponents)
            logic.logic();
    }
}
