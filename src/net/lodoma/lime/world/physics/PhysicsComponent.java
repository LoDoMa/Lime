package net.lodoma.lime.world.physics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;

import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.util.IdentityPool;
import net.lodoma.lime.util.Vector2;
import net.lodoma.lime.world.entity.Entity;

public class PhysicsComponent implements Identifiable<Integer>
{
    public int identifier;
    
    public PhysicsWorld world;
    
    public Entity owner;
    public Body engineBody;
    
    public IdentityPool<PhysicsShape> shapePool;
    
    /* A set of related contact listeners. These are destroyed when the component is destroyed.
       It's a set so that elements can be quickly removed. */
    public Set<PhysicsContactListener> contactListeners = new HashSet<PhysicsContactListener>();
    
    public PhysicsComponent(Vector2 initPosition, float initAngle, PhysicsComponentType type, PhysicsWorld world)
    {
        this.world = world;
        
        shapePool = new IdentityPool<PhysicsShape>(false);
        
        BodyDef bodyDef = new BodyDef();
        bodyDef.position = initPosition.toVec2();
        bodyDef.angle = initAngle;
        bodyDef.type = type.engineType;
        
        engineBody = world.engineWorld.createBody(bodyDef);
        
        // UserData for PhysicsComponents is the PhysicsComponent itself.
        engineBody.m_userData = this;
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
        
        shapePool.foreach((PhysicsShape shape) -> shape.destroy(engineBody));
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
        
        List<PhysicsShapeSnapshot> snapshots = new ArrayList<PhysicsShapeSnapshot>();
        
        shapePool.foreach((PhysicsShape shape) -> {
            if (shape.isRelevantToClient())
                snapshots.addAll(shape.getSnapshots());
        });
        
        snapshot.shapes = new PhysicsShapeSnapshot[snapshots.size()];
        snapshots.toArray(snapshot.shapes);
        
        return snapshot;
    }
}
