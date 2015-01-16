package net.lodoma.lime.world.physics;

import org.jbox2d.dynamics.World;

public class PhysicsWorld
{
    public WorldDefinition definition;
    public org.jbox2d.dynamics.World engineWorld;
    
    public PhysicsWorld()
    {
        definition = new WorldDefinition();
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
}
