package net.lodoma.lime.world.physics;

import net.lodoma.lime.util.Vector2;

import org.jbox2d.collision.shapes.Shape;

public abstract class PhysicsShape
{
    public String name;
    public final Vector2 offset = new Vector2();
    public boolean isSolid = true;
    public float density;
    public float friction;
    public float restitution;
    
    public Shape[] engineInstances;
    
    public void validate() throws InvalidPhysicsShapeException
    {
        if (offset == null)     throw new InvalidPhysicsShapeException("invalid shape position: null");
        if (density < 0)        throw new InvalidPhysicsShapeException("invalid shape density: negative");
        if (friction < 0)       throw new InvalidPhysicsShapeException("invalid shape friction: negative");
        if (restitution < 0)    throw new InvalidPhysicsShapeException("invalid shape restitution: negative");
    }
    
    public abstract void newEngineInstances();
}
