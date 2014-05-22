package net.lodoma.lime.mod.limemod.server;

import net.lodoma.lime.mod.InitBundle;
import net.lodoma.lime.mod.InitPriority;
import net.lodoma.lime.mod.Mod;
import net.lodoma.lime.mod.ModInit;
import net.lodoma.lime.mod.ModTarget;
import net.lodoma.lime.mod.server.Logic;
import net.lodoma.lime.mod.server.LogicPool;
import net.lodoma.lime.net.server.generic.GenericServer;

@Mod(name = "Lime", author = "LoDoMa", target = ModTarget.SERVERSIDE)
public class LimeServerModule implements Logic
{
    @ModInit(priority = InitPriority.INIT)
    public void init(InitBundle bundle)
    {
        GenericServer genericServer = (GenericServer) bundle.get(InitBundle.SERVER);
        LogicPool logicPool = (LogicPool) genericServer.getProperty("logicPool");
        logicPool.addLogicComponent(this);
    }

    @Override
    public void logic()
    {
        
    }
}
