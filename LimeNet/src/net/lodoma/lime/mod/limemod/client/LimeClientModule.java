package net.lodoma.lime.mod.limemod.client;

import net.lodoma.lime.mod.InitBundle;
import net.lodoma.lime.mod.InitPriority;
import net.lodoma.lime.mod.Mod;
import net.lodoma.lime.mod.ModInit;
import net.lodoma.lime.mod.ModTarget;
import net.lodoma.lime.mod.PostinitBundle;
import net.lodoma.lime.mod.PreinitBundle;
import net.lodoma.lime.mod.limemod.chat.CPChatMessage;
import net.lodoma.lime.mod.limemod.chat.CPHChatMessage;
import net.lodoma.lime.mod.limemod.chat.ChatManager;
import net.lodoma.lime.net.LogicPool;
import net.lodoma.lime.net.client.generic.GenericClient;
import net.lodoma.lime.net.packet.generic.ClientPacketPool;

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
