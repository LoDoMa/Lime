package net.lodoma.limemod.base.client;

import net.lodoma.lime.client.generic.net.GenericClient;
import net.lodoma.lime.client.generic.net.packet.ClientPacketPool;
import net.lodoma.lime.common.net.LogicPool;
import net.lodoma.lime.mod.Mod;
import net.lodoma.lime.mod.ModTarget;
import net.lodoma.lime.mod.init.InitBundle;
import net.lodoma.lime.mod.init.InitPriority;
import net.lodoma.lime.mod.init.ModInit;
import net.lodoma.lime.mod.init.PostinitBundle;
import net.lodoma.lime.mod.init.PreinitBundle;
import net.lodoma.limemod.net.chat.ChatManager;
import net.lodoma.limemod.net.chat.packet.client.CPChatMessage;
import net.lodoma.limemod.net.chat.packet.client.CPHChatMessage;

@Mod(name = "Lime::Lime", author = "LoDoMa", target = ModTarget.CLIENTSIDE)
public class LimeClientModule
{
    @ModInit(priority = InitPriority.PREINIT)
    public void preInit(PreinitBundle bundle)
    {
        System.out.println("yo this my preinit");
    }
    
    @ModInit(priority = InitPriority.INIT)
    public void init(InitBundle bundle)
    {
        GenericClient client = (GenericClient) bundle.get(InitBundle.CLIENT);
        
        ClientPacketPool packetPool = (ClientPacketPool) client.getProperty("packetPool");
        packetPool.addPacket("Lime::ChatMessage", new CPChatMessage());
        packetPool.addHandler("Lime::ChatMessage", new CPHChatMessage());
        
        LogicPool logicPool = (LogicPool) client.getProperty("logicPool");
        logicPool.addLogicComponent(new LimeModuleLogic(client));
        
        ChatManager chatManager = new ChatManager(client);
        client.setProperty("chatManager", chatManager);
    }
    
    @ModInit(priority = InitPriority.POSTINIT)
    public void postInit(PostinitBundle bundle)
    {
        System.out.println("yo this my postinit");
    }
}
