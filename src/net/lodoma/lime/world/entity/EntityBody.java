package net.lodoma.lime.world.entity;

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
        components.foreach((BodyComponent component) -> {
            component.destroy();
        });
    }
}
