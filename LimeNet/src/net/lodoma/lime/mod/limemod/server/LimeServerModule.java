package net.lodoma.lime.mod.limemod.server;

import net.lodoma.lime.mod.InitBundle;
import net.lodoma.lime.mod.InitPriority;
import net.lodoma.lime.mod.Mod;
import net.lodoma.lime.mod.ModInit;
import net.lodoma.lime.mod.ModTarget;
import net.lodoma.lime.mod.server.LogicPool;
import net.lodoma.lime.net.packet.SPConnectRequestAnswer;
import net.lodoma.lime.net.packet.SPHConnectRequest;
import net.lodoma.lime.net.packet.generic.ServerPacketPool;
import net.lodoma.lime.net.server.generic.GenericServer;

@Mod(name = "Lime::Lime", author = "LoDoMa", target = ModTarget.SERVERSIDE)
public class LimeServerModule
{
    @ModInit(priority = InitPriority.INIT)
    public void init(InitBundle bundle)
    {
        GenericServer genericServer = (GenericServer) bundle.get(InitBundle.SERVER);
        
        LogicPool logicPool = (LogicPool) genericServer.getProperty("logicPool");
        logicPool.addLogicComponent(new LimeModuleLogic());
        
        ServerPacketPool packetPool = (ServerPacketPool) genericServer.getProperty("packetPool");
        packetPool.addHandler("Lime::ConnectRequest", new SPHConnectRequest());
        packetPool.addPacket("Lime::ConnectRequestAnswer", new SPConnectRequestAnswer());
    }
}
