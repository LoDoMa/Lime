package net.lodoma.lime.world;

import net.lodoma.lime.util.IdentityPool;
import net.lodoma.lime.world.physics.PhysicsComponent;
import net.lodoma.lime.world.physics.PhysicsComponentSnapshot;

public class Terrain
{
    public IdentityPool<PhysicsComponent> physicalComponents;
    public PhysicsComponentSnapshot[] componentSnapshots;
    
    public Terrain()
    {
        physicalComponents = new IdentityPool<PhysicsComponent>(false);
    }
    
    public void debugRender()
    {
        for (PhysicsComponentSnapshot snapshot : componentSnapshots)
            snapshot.debugRender();
    }
}
