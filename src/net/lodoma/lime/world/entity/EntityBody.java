package net.lodoma.lime.world.entity;

import java.util.function.Consumer;

import net.lodoma.lime.util.IdentityPool;

public class EntityBody
{
    public IdentityPool<BodyComponent> components;
    
    public EntityBody()
    {
        components = new IdentityPool<BodyComponent>();
    }
    
    public void destroy()
    {
        components.foreach(new Consumer<BodyComponent>()
        {
            @Override
            public void accept(BodyComponent compo)
            {
                compo.destroy();
            }
        });
    }
}
