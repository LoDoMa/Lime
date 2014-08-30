package net.lodoma.lime.physics;

import net.lodoma.lime.util.Transform;
import net.lodoma.lime.util.Vector2;

public class PhysicsObject
{
    private Transform transform;
    private Vector2 velocity;
    private Collider collider;
    
    public PhysicsObject(Transform transform, Vector2 velocity, Collider collider)
    {
        this.transform = transform;
        this.velocity = velocity;
        this.collider = collider;
    }
    
    public Transform getTransform()
    {
        return transform;
    }
    
    public Vector2 getVelocity()
    {
        return velocity;
    }
    
    public Collider getCollider()
    {
        return collider;
    }
    
    public IntersectData collide(PhysicsObject other)
    {
        return collider.collide(transform, other.collider, other.transform);
    }
}
