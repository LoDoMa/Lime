package net.lodoma.lime.mod.limemod;

import net.lodoma.lime.mod.InitBundle;
import net.lodoma.lime.mod.InitPriority;
import net.lodoma.lime.mod.Mod;
import net.lodoma.lime.mod.ModInit;
import net.lodoma.lime.mod.ModTarget;
import net.lodoma.lime.mod.PostinitBundle;
import net.lodoma.lime.mod.PreinitBundle;
import net.lodoma.lime.net.client.LimeClient;
import net.lodoma.lime.net.packet.CPConnectRequest;
import net.lodoma.lime.net.packet.CPDependencyRequest;
import net.lodoma.lime.net.packet.CPHConnectRequestAnswer;
import net.lodoma.lime.net.packet.dependency.CPHModuleDependency;
import net.lodoma.lime.net.packet.dependency.CPHUserStatus;
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
        LimeClient client = (LimeClient) bundle.get(InitBundle.CLIENT);
        
        ClientPacketPool packetPool = (ClientPacketPool) client.getProperty("packetPool");
        packetPool.addPacket("Lime::ConnectRequest", new CPConnectRequest());
        packetPool.addHandler("Lime::ConnectRequestAnswer", new CPHConnectRequestAnswer());
        packetPool.addPacket("Lime::DependencyRequest", new CPDependencyRequest());
        packetPool.addHandler("Lime::UserStatus", new CPHUserStatus());
        packetPool.addHandler("Lime::ModuleDependency", new CPHModuleDependency());
        
        packetPool.getPacket("Lime::ConnectRequest").send(client);
        System.out.println("yo this my init");
    }
    
    @ModInit(priority = InitPriority.POSTINIT)
    public void postInit(PostinitBundle bundle)
    {
        System.out.println("yo this my postinit");
    }
}
