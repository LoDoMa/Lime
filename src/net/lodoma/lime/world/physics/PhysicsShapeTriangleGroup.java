package net.lodoma.lime.world.physics;

import java.util.ArrayList;
import java.util.List;

import net.lodoma.lime.util.Vector2;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

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
        
        if (triangles == null) throw new InvalidPhysicsShapeException("invalid shape triangle group: triangle list is null");
        
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
    public void create(Body engineBody)
    {
        super.create(engineBody);
        
        engineFixtures = new Fixture[0];
    }
    
    protected void createFixture(int index)
    {
        PolygonShape shape = new PolygonShape();
        Vector2[] vertices = triangles.get(index);
        Vec2[] engineVertices = new Vec2[3];
        for (int i = 0; i < 3; i++)
            engineVertices[i] = new Vec2(offset.x + vertices[i].x, offset.y + vertices[i].y);
        shape.set(engineVertices, engineVertices.length);
        
        FixtureDef def = new FixtureDef();
        def.shape = shape;
        def.userData = this;
        
        engineFixtures[index] = engineBody.createFixture(def);
    }
    
    @Override
    public void update()
    {
        if (engineFixtures.length < triangles.size())
        {
            Fixture[] oldArray = engineFixtures;
            engineFixtures = new Fixture[triangles.size()];
            System.arraycopy(oldArray, 0, engineFixtures, 0, oldArray.length);
            
            for (int i = oldArray.length; i < engineFixtures.length; i++)
                createFixture(i);
        }
        
        for (int i = 0; i < engineFixtures.length; i++)
        {
            PolygonShape shape = (PolygonShape) engineFixtures[i].m_shape;
            Vector2[] triangle = triangles.get(i);
            
            boolean recreate = false;
            
            for (int j = 0; j < 3; j++)
            {
                Vec2 current = shape.m_vertices[j];
                if ((current.x != offset.x + triangle[i].x) || (current.y != offset.y + triangle[i].y))
                {
                    recreate = true;
                    break;
                }
            }
            
            if (recreate)
            {
                engineBody.destroyFixture(engineFixtures[i]);
                createFixture(i);
            }
        }
        
        super.update();
    }
    
    @Override
    protected PhysicsShapeSnapshot buildSnapshot(int shapeIndex)
    {
        PhysicsShapeSnapshot snapshot = super.buildSnapshot(shapeIndex);
        Fixture fixture = engineFixtures[shapeIndex];
        
        snapshot.shapeType = PhysicsShapeType.POLYGON;
        Vec2[] engineVertices = ((PolygonShape) fixture.m_shape).m_vertices;
        snapshot.vertices = new Vector2[((PolygonShape) fixture.m_shape).m_count];
        for (int i = 0; i < snapshot.vertices.length; i++)
            snapshot.vertices[i] = new Vector2(engineVertices[i]);
        
        return snapshot;
    }
}
