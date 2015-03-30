package net.lodoma.lime.world.physics;

import java.util.ArrayList;
import java.util.List;

import net.lodoma.lime.util.Vector2;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;

public class PhysicsShapeTriangleGroup extends PhysicsShape
{
    public List<Vector2[]> triangles;
    
    public PhysicsShapeTriangleGroup()
    {
        triangles = new ArrayList<Vector2[]>();
    }
    
    @Override
    public void validate() throws InvalidPhysicsShapeException
    {
        super.validate();
        
        if (triangles == null)       throw new InvalidPhysicsShapeException("invalid shape triangle group: triangle list is null");
        if (triangles.isEmpty())     throw new InvalidPhysicsShapeException("invalid shape triangle group: empty triangle list");
        for (int i = 0; i < triangles.size(); i++)
        {
            Vector2[] triangle = triangles.get(i);
            if (triangle == null)
                throw new InvalidPhysicsShapeException("invalid shape triangle group: element #" + (i + 1) + " is null");
            if (triangle.length != 3)
                throw new InvalidPhysicsShapeException("invalid shape triangle group: element #" + (i + 1) + " is not a triangle");
            for (int j = 0; j < 3; j++)
                if (triangle[j] == null)
                    throw new InvalidPhysicsShapeException("invalid shape triangle group: element #" + (i + 1) + " is incomplete");
        }
    }
    
    @Override
    public void newEngineInstances()
    {
        PolygonShape[] shapes = new PolygonShape[triangles.size()];
        for (int i = 0; i < shapes.length; i++)
        {
            shapes[i] = new PolygonShape();
            
            Vector2[] vertices = triangles.get(i);
            Vec2[] engineVertices = new Vec2[3];
            for (int j = 0; j < 3; j++)
                engineVertices[j] = new Vec2(offset.x + vertices[j].x, offset.y + vertices[j].y);
            
            shapes[i].set(engineVertices, engineVertices.length);
        }
        
        engineInstances = shapes;
    }
}
