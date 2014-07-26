package net.lodoma.lime.physics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class PhysicsWorld
{
    private static final Vec2 ENGINE_GRAVITY = new Vec2(0.0f, -0.1f);
    private static final boolean ENGINE_SLEEP = false;
    
    private static final int ENGINE_VELOCITY_ITERATIONS = 6;
    private static final int ENGINE_POSITION_ITERATIONS = 2;
    
    private World world;
    
    public PhysicsWorld()
    {
        world = new World(ENGINE_GRAVITY, ENGINE_SLEEP);
    }
    
    public World getEngineWorld()
    {
        return world;
    }
    
    public void update(double timeDelta)
    {
        world.step((float) timeDelta, ENGINE_VELOCITY_ITERATIONS, ENGINE_POSITION_ITERATIONS);
    }
}
