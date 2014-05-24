package net.lodoma.lime.mod.limemod.server;

import net.lodoma.lime.mod.InitBundle;
import net.lodoma.lime.mod.InitPriority;
import net.lodoma.lime.mod.Mod;
import net.lodoma.lime.mod.ModInit;
import net.lodoma.lime.mod.ModTarget;
import net.lodoma.lime.mod.Module;
import net.lodoma.lime.mod.limemod.chat.SPChatMessage;
import net.lodoma.lime.mod.limemod.chat.SPHChatMessage;
import net.lodoma.lime.net.LogicPool;
import net.lodoma.lime.net.packet.generic.ServerPacketPool;
import net.lodoma.lime.net.server.generic.GenericServer;

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
        
        Module module = (Module) bundle.get(InitBundle.MODULE);
        module.addClientModuleDependency("Lime::Lime");
        
        LogicPool logicPool = (LogicPool) server.getProperty("logicPool");
        logicPool.addLogicComponent(new LimeModuleLogic());
    }
}
