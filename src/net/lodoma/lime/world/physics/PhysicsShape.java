package net.lodoma.lime.world.physics;

import net.lodoma.lime.util.Vector2;

import org.jbox2d.collision.shapes.Shape;

public abstract class PhysicsShape
{
    public final Vector2 offset = new Vector2();
    public boolean isSolid = true;
    public float density;
    public float friction;
    public float restitution;
    
    public Shape[] engineInstances;
    
    public void validate() throws InvalidPhysicsComponentException
    {
        if (offset == null)        throw new InvalidPhysicsComponentException("invalid shape position: null");
        if (density < 0)        throw new InvalidPhysicsComponentException("invalid shape density: negative");
        if (friction < 0)       throw new InvalidPhysicsComponentException("invalid shape friction: negative");
        if (restitution < 0)    throw new InvalidPhysicsComponentException("invalid shape restitution: negative");
    }
    
    public abstract void newEngineInstances();
}
