package net.lodoma.limemod.base.client;

import net.lodoma.lime.mod.Mod;
import net.lodoma.lime.mod.ModTarget;

@Mod(name = "Lime::Lime", author = "LoDoMa", target = ModTarget.CLIENTSIDE)
public class LimeClientModule
{/*
    @ModInit(priority = InitPriority.PREINIT)
    public void preInit(PreinitBundle bundle)
    {
        System.out.println("yo this my preinit");
    }
    
    @ModInit(priority = InitPriority.INIT)
    public void init(InitBundle bundle)
    {
        GenericClient client = (GenericClient) bundle.get(InitBundle.CLIENT);
        ClientData data = client.getData();
        
        ClientPacketPool packetPool = data.packetPool;
        packetPool.addPacket("Lime::ChatMessage", new CPChatMessage());
        packetPool.addHandler("Lime::ChatMessage", new CPHChatMessage());
        
        data.logicPool.addLogicComponent(new BaseLogic(client));
        data.chatManager = new ChatManager(client);
    }
    
    @ModInit(priority = InitPriority.POSTINIT)
    public void postInit(PostinitBundle bundle)
    {
        System.out.println("yo this my postinit");
    }*/
}
