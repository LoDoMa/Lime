package net.lodoma.lime.world;

import net.lodoma.lime.util.IdentityPool;
import net.lodoma.lime.world.physics.PhysicsComponent;
import net.lodoma.lime.world.physics.PhysicsComponentSnapshot;

public class Terrain
{
    public Object updateLock = new Object();
    
    public IdentityPool<PhysicsComponent> physicalComponents;
    public PhysicsComponentSnapshot[] componentSnapshots;
    
    public Terrain()
    {
        physicalComponents = new IdentityPool<PhysicsComponent>(false);
    }
    
    public void debugRender()
    {
        if (componentSnapshots == null)
            return;
        
        synchronized (updateLock)
        {
            for (PhysicsComponentSnapshot snapshot : componentSnapshots)
                snapshot.debugRender();
        }
    }
}
