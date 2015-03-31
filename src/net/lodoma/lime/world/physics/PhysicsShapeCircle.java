package net.lodoma.lime.world.physics;

import net.lodoma.lime.util.Vector2;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

public class PhysicsShapeCircle extends PhysicsShape
{
    public float radius;

    @Override
    public void validate() throws InvalidPhysicsShapeException
    {
        super.validate();
        
        if (radius < 0) throw new InvalidPhysicsShapeException("invalid shape circle radius: negative");
    }
    
    protected void createFixture(int index)
    {
        CircleShape shape = new CircleShape();
        shape.m_p.set(offset.x, offset.y);
        shape.m_radius = radius;
        
        FixtureDef def = new FixtureDef();
        def.shape = shape;
        def.userData = this;
        
        engineFixtures[index] = engineBody.createFixture(def);
    }
    
    @Override
    public void create(Body engineBody)
    {
        super.create(engineBody);
        
        engineFixtures = new Fixture[1];
        createFixture(0);
    }
    
    @Override
    public void update()
    {
        super.update();        
        
        CircleShape shape = (CircleShape) engineFixtures[0].m_shape;
        
        if (shape.m_p.x != offset.x || shape.m_p.y != offset.y || shape.m_radius != radius)
        {
            engineBody.destroyFixture(engineFixtures[0]);
            createFixture(0);
        }
    }
    
    @Override
    protected PhysicsShapeSnapshot buildSnapshot(int shapeIndex)
    {
        PhysicsShapeSnapshot snapshot = super.buildSnapshot(shapeIndex);
        Fixture fixture = engineFixtures[shapeIndex];
        
        snapshot.shapeType = PhysicsShapeType.CIRCLE;
        snapshot.offset = new Vector2(((CircleShape) fixture.m_shape).m_p);
        snapshot.radius = ((CircleShape) fixture.m_shape).m_radius;
        
        return snapshot;
    }
}
