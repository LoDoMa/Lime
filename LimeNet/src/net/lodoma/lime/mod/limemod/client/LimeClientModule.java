package net.lodoma.lime.mod.limemod.client;

import net.lodoma.lime.mod.InitBundle;
import net.lodoma.lime.mod.InitPriority;
import net.lodoma.lime.mod.Mod;
import net.lodoma.lime.mod.ModInit;
import net.lodoma.lime.mod.ModTarget;
import net.lodoma.lime.mod.PostinitBundle;
import net.lodoma.lime.mod.PreinitBundle;

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
        System.out.println("yo this my init");
    }
    
    @ModInit(priority = InitPriority.POSTINIT)
    public void postInit(PostinitBundle bundle)
    {
        System.out.println("yo this my postinit");
    }
}
