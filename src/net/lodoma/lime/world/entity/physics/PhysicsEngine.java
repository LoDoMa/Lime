package net.lodoma.lime.world.entity.physics;

import java.util.function.Consumer;

import net.lodoma.lime.world.World;
import net.lodoma.lime.world.entity.Entity;

public class PhysicsEngine
{
    private World world;
    
    public PhysicsEngine(World world)
    {
        this.world = world;
    }
    
    public void update(double timeDelta)
    {
        world.entityPool.foreach(new Consumer<Entity>()
        {
            @Override
            public void accept(Entity entity)
            {
                entity.update((float) timeDelta);
            }
        });
    }
}
