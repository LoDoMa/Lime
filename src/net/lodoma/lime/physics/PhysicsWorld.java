package net.lodoma.lime.physics;

import java.util.function.Consumer;

import net.lodoma.lime.util.GeneratedIdentityPool;

public class PhysicsWorld
{
    public GeneratedIdentityPool<Body> bodyPool;
    public GeneratedIdentityPool<Platform> platformPool;
    
    public PhysicsWorld()
    {
        bodyPool = new GeneratedIdentityPool<Body>();
        platformPool = new GeneratedIdentityPool<Platform>();
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
