package net.lodoma.limemod.base.server;

import net.lodoma.lime.common.net.LogicPool;
import net.lodoma.lime.mod.Mod;
import net.lodoma.lime.mod.ModTarget;
import net.lodoma.lime.mod.Module;
import net.lodoma.lime.mod.init.InitBundle;
import net.lodoma.lime.mod.init.InitPriority;
import net.lodoma.lime.mod.init.ModInit;
import net.lodoma.lime.server.generic.GenericServer;
import net.lodoma.lime.server.generic.net.packet.ServerPacketPool;
import net.lodoma.limemod.net.chat.packet.server.SPChatMessage;
import net.lodoma.limemod.net.chat.packet.server.SPHChatMessage;

@Mod(name = "Lime::Lime", author = "LoDoMa", target = ModTarget.SERVERSIDE)
public class LimeServerModule
{
    @ModInit(priority = InitPriority.INIT)
    public void init(InitBundle bundle)
    {
        GenericServer server = (GenericServer) bundle.get(InitBundle.SERVER);
        
        ServerPacketPool packetPool = (ServerPacketPool) server.getProperty("packetPool");
        packetPool.addPacket("Lime::ChatMessage", new SPChatMessage());
        packetPool.addHandler("Lime::ChatMessage", new SPHChatMessage());
        
        LogicPool logicPool = (LogicPool) server.getProperty("logicPool");
        logicPool.addLogicComponent(new LimeModuleLogic());
    }
}
