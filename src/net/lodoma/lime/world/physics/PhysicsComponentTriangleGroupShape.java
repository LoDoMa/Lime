package net.lodoma.lime.world.physics;

import java.util.ArrayList;
import java.util.List;

import net.lodoma.lime.util.Vector2;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;

public class PhysicsComponentTriangleGroupShape extends PhysicsComponentShape
{
    public List<Vector2[]> triangles;
    
    public PhysicsComponentTriangleGroupShape()
    {
        triangles = new ArrayList<Vector2[]>();
    }
    
    @Override
    public void validate() throws InvalidPhysicsComponentException
    {
        if (triangles == null)       throw new InvalidPhysicsComponentException("invalid component triangle group shape: triangle list is null");
        if (triangles.isEmpty())     throw new InvalidPhysicsComponentException("invalid component triangle group shape: empty triangle list");
        for (int i = 0; i < triangles.size(); i++)
        {
            Vector2[] triangle = triangles.get(i);
            if (triangle == null)
                throw new InvalidPhysicsComponentException("invalid component triangle group shape: element #" + (i + 1) + " is null");
            if (triangle.length != 3)
                throw new InvalidPhysicsComponentException("invalid component triangle group shape: element #" + (i + 1) + " is not a triangle");
            for (int j = 0; j < 3; j++)
                if (triangle[j] == null)
                    throw new InvalidPhysicsComponentException("invalid component triangle group shape: element #" + (i + 1) + " is incomplete");
        }
    }
    
    @Override
    public Shape[] newEngineInstances()
    {
        PolygonShape[] shapes = new PolygonShape[triangles.size()];
        for (int i = 0; i < shapes.length; i++)
        {
            shapes[i] = new PolygonShape();
            
            Vector2[] vertices = triangles.get(i);
            Vec2[] engineVertices = new Vec2[3];
            for (int j = 0; j < 3; j++)
                engineVertices[j] = vertices[j].toVec2();
            
            shapes[i].set(engineVertices, engineVertices.length);
        }
        return shapes;
    }
}
