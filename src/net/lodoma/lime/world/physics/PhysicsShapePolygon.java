package net.lodoma.lime.world.physics;

import net.lodoma.lime.util.Vector2;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Settings;
import org.jbox2d.common.Vec2;

public class PhysicsShapePolygon extends PhysicsShape
{
    public Vector2 vertices[];
    
    @Override
    public void validate() throws InvalidPhysicsComponentException
    {
        super.validate();
        
        if (vertices == null)    throw new InvalidPhysicsComponentException("invalid component polygon shape vertices: null");
        if (vertices.length < 3) throw new InvalidPhysicsComponentException("invalid component polygon shape vertices: less than 3");
        if (vertices.length > Settings.maxPolygonVertices)
            throw new InvalidPhysicsComponentException("invalid component polygon shape vertices: more than " + Settings.maxPolygonVertices);
    }
    
    @Override
    public void newEngineInstances()
    {
        PolygonShape shape = new PolygonShape();
        Vec2[] engineVertices = new Vec2[vertices.length];
        for (int i = 0; i < vertices.length; i++)
            engineVertices[i] = new Vec2(offset.x + vertices[i].x, offset.y + vertices[i].y);
        shape.set(engineVertices, engineVertices.length);
        
        engineInstances = new Shape[] { shape };
    }
}
