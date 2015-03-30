package net.lodoma.lime.world.physics;

import org.jbox2d.collision.shapes.Shape;

public abstract class PhysicsShape
{
    public float density;
    public float friction;
    public float restitution;
    
    public Shape[] engineInstances;
    
    public void validate() throws InvalidPhysicsComponentException
    {
        if (density < 0)        throw new InvalidPhysicsComponentException("invalid component density: negative");
        if (friction < 0)       throw new InvalidPhysicsComponentException("invalid component friction: negative");
        if (restitution < 0)    throw new InvalidPhysicsComponentException("invalid component restitution: negative");
    }
    
    public abstract void newEngineInstances();
}
