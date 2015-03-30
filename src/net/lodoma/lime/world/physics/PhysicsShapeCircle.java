package net.lodoma.lime.world.physics;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.Shape;

public class PhysicsShapeCircle extends PhysicsShape
{
    public float radius;

    @Override
    public void validate() throws InvalidPhysicsShapeException
    {
        super.validate();
        
        if (radius < 0) throw new InvalidPhysicsShapeException("invalid shape circle radius: negative");
    }
    
    @Override
    public void newEngineInstances()
    {
        CircleShape shape = new CircleShape();
        shape.m_p.set(offset.x, offset.y);
        shape.m_radius = radius;
        
        engineInstances = new Shape[] { shape };
    }
}
