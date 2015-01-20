package net.lodoma.lime.world.physics;

import net.lodoma.lime.util.IdentityPool;

import org.jbox2d.dynamics.World;

public class PhysicsWorld
{
    public WorldDefinition definition;
    public org.jbox2d.dynamics.World engineWorld;
    
    public IdentityPool<PhysicsJoint> jointPool;
    
    public PhysicsWorld()
    {
        definition = new WorldDefinition();
        
        jointPool = new IdentityPool<PhysicsJoint>(false);
    }
    
    public void create()
    {
        engineWorld = new World(definition.gravity.toVec2());
        engineWorld.setAllowSleep(false);
    }
    
    public void update(float timeDelta)
    {
        engineWorld.step((float) timeDelta, 6, 2);
    }
    
    public void destroy()
    {
        jointPool.foreach((PhysicsJoint joint) -> joint.destroy());
    }
}
