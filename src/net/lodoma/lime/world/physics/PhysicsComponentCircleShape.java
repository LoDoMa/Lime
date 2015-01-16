package net.lodoma.lime.world.physics;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.Shape;

public class PhysicsComponentCircleShape extends PhysicsComponentShape
{
    public float radius;
    
    @Override
    public Shape newEngineInstance()
    {
        CircleShape shape = new CircleShape();
        shape.m_radius = radius;
        return shape;
    }
}
