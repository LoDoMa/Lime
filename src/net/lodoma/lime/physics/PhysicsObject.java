package net.lodoma.lime.physics;

import net.lodoma.lime.util.Vector2;

public class PhysicsObject
{
    private Vector2 position;
    private Vector2 velocity;
    private Collider collider;
    
    public PhysicsObject(Vector2 position, Collider collider)
    {
        this(position, new Vector2(0.0f, 0.0f), collider);
    }
    
    public PhysicsObject(Vector2 position, Vector2 velocity, Collider collider)
    {
        this.position = position;
        this.velocity = velocity;
        this.collider = collider;
    }
    
    public Vector2 getPosition()
    {
        return position;
    }
    
    public Vector2 getVelocity()
    {
        return velocity;
    }
    
    public Collider getCollider()
    {
        return collider;
    }
    
    public void setVelocity(Vector2 velocity)
    {
        this.velocity = velocity;
    }
}
