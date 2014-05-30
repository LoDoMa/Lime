package net.lodoma.limemod.world.server;

import net.lodoma.lime.mod.Mod;
import net.lodoma.lime.mod.ModTarget;
import net.lodoma.lime.mod.init.InitBundle;
import net.lodoma.lime.mod.init.InitPriority;
import net.lodoma.lime.mod.init.ModInit;
import net.lodoma.lime.mod.init.PostinitBundle;
import net.lodoma.lime.mod.init.PreinitBundle;

@Mod(name = "Lime::World", target = ModTarget.SERVERSIDE)
public class WorldServerModule
{
    @ModInit(priority = InitPriority.PREINIT)
    public void preinit(PreinitBundle bundle)
    {
        
    }

    @ModInit(priority = InitPriority.INIT)
    public void init(InitBundle bundle)
    {
        
    }

    @ModInit(priority = InitPriority.POSTINIT)
    public void postinit(PostinitBundle bundle)
    {
        
    }
}
