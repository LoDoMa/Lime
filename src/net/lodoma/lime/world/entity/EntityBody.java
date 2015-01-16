package net.lodoma.lime.world.entity;

import net.lodoma.lime.util.IdentityPool;
import net.lodoma.lime.world.physics.PhysicsComponent;

public class EntityBody
{
    public IdentityPool<PhysicsComponent> components;
    
    public EntityBody()
    {
        components = new IdentityPool<PhysicsComponent>();
    }
    
    public void destroy()
    {
        components.foreach((PhysicsComponent component) -> {
            component.destroy();
        });
    }
}
