package net.lodoma.limemod.world.client;

import net.lodoma.lime.mod.Mod;
import net.lodoma.lime.mod.ModTarget;

@Mod(name = "Lime::World", target = ModTarget.CLIENTSIDE)
public class WorldClientModule
{/*
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
        
    }*/
}