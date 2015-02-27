package net.lodoma.lime.world.physics;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.util.Vector2;

public class PhysicsComponent implements Identifiable<Integer>
{
    public int identifier;
    
    public PhysicsWorld world;
    
    public Body engineBody;
    public Fixture engineFixture;
    
    /* A set of related contact listeners. These are destroyed when the component is destroyed.
       It's a set so that elements can be quickly removed. */
    public Set<PhysicsContactListener> contactListeners = new HashSet<PhysicsContactListener>();
    
    public PhysicsComponent(PhysicsComponentDefinition definition, PhysicsWorld world)
    {
        this.world = world;
        
        engineBody = world.engineWorld.createBody(definition.engineBodyDefinition);
        engineFixture = engineBody.createFixture(definition.engineFixtureDefinition);
    }
    
    public PhysicsComponent(PhysicsComponentSnapshot snapshot, PhysicsWorld world)
    {
        this(snapshot.createDefinition(), world);
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
        
        // UserData for PhysicsComponents is the PhysicsComponent itself.
        engineBody.m_userData = this;
    }
    
    public void destroy()
    {
        Iterator<PhysicsContactListener> iterator = contactListeners.iterator();
        while (iterator.hasNext())
        {
            PhysicsContactListener contactListener = iterator.next();
            contactListener.destroy();
        }
        
        engineBody.destroyFixture(engineFixture);
        world.engineWorld.destroyBody(engineBody);
    }
    
    public PhysicsComponentSnapshot createSnapshot()
    {
        PhysicsComponentSnapshot snapshot = new PhysicsComponentSnapshot();
        snapshot.position = new Vector2(engineBody.getPosition().x, engineBody.getPosition().y);
        snapshot.angle = engineBody.getAngle();

        if (engineBody.m_type == BodyType.DYNAMIC)
            snapshot.type = PhysicsComponentType.DYNAMIC;
        else if (engineBody.m_type == BodyType.KINEMATIC)
            snapshot.type = PhysicsComponentType.KINEMATIC;
        else if (engineBody.m_type == BodyType.STATIC)
            snapshot.type = PhysicsComponentType.STATIC;
        
        if (engineFixture.m_shape.m_type == ShapeType.CIRCLE)
            snapshot.shapeType = PhysicsComponentShapeType.CIRCLE;
        else if (engineFixture.m_shape.m_type == ShapeType.POLYGON)
            snapshot.shapeType = PhysicsComponentShapeType.POLYGON;
        
        switch (snapshot.shapeType)
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
