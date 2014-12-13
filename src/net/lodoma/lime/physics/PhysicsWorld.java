package net.lodoma.lime.physics;

import java.util.function.Consumer;

import net.lodoma.lime.util.IdentityPool;

public class PhysicsWorld
{
    public IdentityPool<Body> bodyPool;
    public IdentityPool<Platform> platformPool;
    
    public PhysicsWorld()
    {
        bodyPool = new IdentityPool<Body>();
        platformPool = new IdentityPool<Platform>();
    }
    
    public void update(float timeDelta)
    {
        // NOTE: The Consumer here should probably be in a final field
        bodyPool.foreach(new Consumer<Body>()
        {
            public void accept(Body body)
            {
                body.simulate(timeDelta);
            }
        });
    }
    
    public void debugRender()
    {
        // NOTE: The Consumer here should probably be in a final field
        bodyPool.foreach(new Consumer<Body>()
        {
            public void accept(Body body)
            {
                body.debugRender();
            }
        });
    }
}
