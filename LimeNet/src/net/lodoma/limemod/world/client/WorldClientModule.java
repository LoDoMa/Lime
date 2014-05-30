package net.lodoma.limemod.world.client;

import net.lodoma.lime.client.generic.net.GenericClient;
import net.lodoma.lime.mod.Mod;
import net.lodoma.lime.mod.ModTarget;
import net.lodoma.lime.mod.init.InitBundle;
import net.lodoma.lime.mod.init.InitPriority;
import net.lodoma.lime.mod.init.ModInit;
import net.lodoma.lime.mod.init.PostinitBundle;
import net.lodoma.lime.mod.init.PreinitBundle;

@Mod(name = "Lime::World", target = ModTarget.CLIENTSIDE)
public class WorldClientModule
{
    @ModInit(priority = InitPriority.PREINIT)
    public void preinit(PreinitBundle bundle)
    {
        
    }

    @ModInit(priority = InitPriority.INIT)
    public void init(InitBundle bundle)
    {
        GenericClient client = (GenericClient) bundle.get(InitBundle.CLIENT);
        if(!client.getData().modulePool.isModuleLoaded("Lime::Lime"))
            System.out.println("missing dependency");
    }

    @ModInit(priority = InitPriority.POSTINIT)
    public void postinit(PostinitBundle bundle)
    {
        
    }
}