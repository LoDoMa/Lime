package net.lodoma.lime.world.physics;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.Shape;

public class PhysicsComponentCircleShape extends PhysicsComponentShape
{
    public float radius;

    @Override
    public void validate() throws InvalidPhysicsComponentException
    {
        if (radius < 0) throw new InvalidPhysicsComponentException("invalid component circle shape radius: negative");
    }
    
    @Override
    public Shape newEngineInstance()
    {
        CircleShape shape = new CircleShape();
        shape.m_radius = radius;
        return shape;
    }
}
