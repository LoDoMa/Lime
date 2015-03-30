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
import org.jbox2d.dynamics.FixtureDef;

import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.util.Vector2;

public class PhysicsComponent implements Identifiable<Integer>
{
    public int identifier;
    
    public PhysicsWorld world;
    
    public Body engineBody;
    public Fixture[] engineFixtures;
    
    /* A set of related contact listeners. These are destroyed when the component is destroyed.
       It's a set so that elements can be quickly removed. */
    public Set<PhysicsContactListener> contactListeners = new HashSet<PhysicsContactListener>();
    
    public PhysicsComponent(PhysicsComponentDefinition definition, PhysicsWorld world)
    {
        this.world = world;
        
        engineBody = world.engineWorld.createBody(definition.engineBodyDefinition);
        
        FixtureDef fixtureDefs[] = definition.engineFixtureDefinitions;
        engineFixtures = new Fixture[fixtureDefs.length];
        for (int i = 0; i < fixtureDefs.length; i++)
            engineFixtures[i] = engineBody.createFixture(fixtureDefs[i]);
        
        // UserData for PhysicsComponents is the PhysicsComponent itself.
        engineBody.m_userData = this;
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
    }
    
    public void destroy()
    {
        Iterator<PhysicsContactListener> iterator = contactListeners.iterator();
        while (iterator.hasNext())
        {
            PhysicsContactListener contactListener = iterator.next();
            contactListener.destroy();
        }
        
        for (int i = 0; i < engineFixtures.length; i++)
            engineBody.destroyFixture(engineFixtures[i]);
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
        
        snapshot.shapes = new PhysicsShapeSnapshot[engineFixtures.length];
        for (int shapeI = 0; shapeI < snapshot.shapes.length; shapeI++)
        {
            snapshot.shapes[shapeI] = new PhysicsShapeSnapshot();
            
            if (engineFixtures[0].m_shape.m_type == ShapeType.CIRCLE)
                snapshot.shapes[shapeI].shapeType = PhysicsShapeType.CIRCLE;
            else if (engineFixtures[0].m_shape.m_type == ShapeType.POLYGON)
                snapshot.shapes[shapeI].shapeType = PhysicsShapeType.POLYGON;
            
            switch (snapshot.shapes[shapeI].shapeType)
            {
            case CIRCLE:
            {
                snapshot.shapes[shapeI].radius = ((CircleShape) engineFixtures[0].m_shape).m_radius;
                break;
            }
            case POLYGON:
            {
                Vec2[] engineVertices = ((PolygonShape) engineFixtures[0].m_shape).m_vertices;
                snapshot.shapes[shapeI].vertices = new Vector2[((PolygonShape) engineFixtures[0].m_shape).m_count];
                for (int i = 0; i < snapshot.shapes[shapeI].vertices.length; i++)
                    snapshot.shapes[shapeI].vertices[i] = new Vector2(engineVertices[i].x, engineVertices[i].y);
                break;
            }
            default:
                throw new IllegalStateException();
            }
        }
        
        return snapshot;
    }
}
