package net.lodoma.lime.world.physics;

import org.jbox2d.dynamics.World;

public class PhysicsWorld
{
    public WorldDefinition definition;
    public org.jbox2d.dynamics.World engineWorld;
    public PhysicsContactManager contactManager;
    
    public PhysicsWorld()
    {
        definition = new WorldDefinition();
    }
    
    public void create()
    {
        contactManager = new PhysicsContactManager();
        
        engineWorld = new World(definition.gravity.toVec2());
        engineWorld.setAllowSleep(false);
        engineWorld.setContactListener(contactManager);
    }
    
    public void update(float timeDelta)
    {
        engineWorld.step((float) timeDelta, 6, 2);
    }
    
    public void destroy()
    {
        
    }
}
