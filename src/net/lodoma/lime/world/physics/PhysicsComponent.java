package net.lodoma.lime.world.physics;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.util.Vector2;

public class PhysicsComponent implements Identifiable<Integer>
{
    public int identifier;
    
    public PhysicsWorld world;
    
    public Body engineBody;
    public Fixture engineFixture;
    
    public PhysicsComponent(PhysicsComponentDefinition definition, PhysicsWorld world)
    {
        this.world = world;
        
        engineBody = world.engineWorld.createBody(definition.engineBodyDefinition);
        engineFixture = engineBody.createFixture(definition.engineFixtureDefinition);
        
        // UserData for JBox2D bodies is the PhysicsComponent ID.
        engineBody.m_userData = identifier;
    }
    
    @Override
    public Integer getIdentifier()
    {
        return identifier;
    }
    
    @Override
    public void setIdentifier(Integer identifier)
    {
        this.identifier = identifier;
    }
    
    public void destroy()
    {
        engineBody.destroyFixture(engineFixture);
        world.engineWorld.destroyBody(engineBody);
    }
    
    public PhysicsComponentSnapshot createSnapshot()
    {
        PhysicsComponentSnapshot snapshot = new PhysicsComponentSnapshot();
        snapshot.position = new Vector2(engineBody.getPosition().x, engineBody.getPosition().y);
        snapshot.angle = engineBody.getAngle();

        if (engineFixture.m_shape.m_type == ShapeType.CIRCLE)
            snapshot.type = PhysicsComponentShapeType.CIRCLE;
        if (engineFixture.m_shape.m_type == ShapeType.POLYGON)
            snapshot.type = PhysicsComponentShapeType.POLYGON;
        
        switch (snapshot.type)
        {
        case CIRCLE:
        {
            snapshot.radius = ((CircleShape) engineFixture.m_shape).m_radius;
            break;
        }
        case POLYGON:
        {
            Vec2[] engineVertices = ((PolygonShape) engineFixture.m_shape).m_vertices;
            snapshot.vertices = new Vector2[((PolygonShape) engineFixture.m_shape).m_count];
            for (int i = 0; i < snapshot.vertices.length; i++)
                snapshot.vertices[i] = new Vector2(engineVertices[i].x, engineVertices[i].y);
            break;
        }
        }
        
        return snapshot;
    }
}
