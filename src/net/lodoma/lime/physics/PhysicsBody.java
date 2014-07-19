package net.lodoma.lime.physics;

import java.io.DataOutputStream;

import net.lodoma.lime.util.Vector2;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

public class PhysicsBody
{
    private BodyDef bd;
    private Shape shape;
    private FixtureDef fd;
    
    private Fixture fixture;
    private Body body;
    
    public PhysicsBody()
    {
        bd = new BodyDef();
        fd = new FixtureDef();
    }
    
    public Vector2 getPosition()
    {
        return new Vector2(body.getPosition());
    }
    
    public float getAngle()
    {
        return body.getAngle();
    }
    
    public void setPosition(Vector2 pos)
    {
        bd.position.set(pos.x, pos.y);
    }
    
    public void setBodyType(PhysicsBodyType type)
    {
        bd.type = type.getEngineValue();
    }
    
    public void setCircleShape(float radius)
    {
        shape = new CircleShape();
        shape.m_radius = radius;
        fd.shape = shape;
    }
    
    public void setPolygonShape(Vector2... vertices)
    {
        shape = new PolygonShape();
        ((PolygonShape) shape).set(Vector2.toVec2Array(vertices), vertices.length);
        fd.shape = shape;
    }
    
    public void setDensity(float density)
    {
        fd.density = density;
    }
    
    public void setFriction(float friction)
    {
        fd.friction = friction;
    }
    
    public void setRestitution(float restitution)
    {
        fd.restitution = restitution;
    }
    
    public void applyLinearImpulse(Vector2 impulse, Vector2 point)
    {
        body.applyLinearImpulse(new Vec2(impulse.x, impulse.y), new Vec2(point.x, point.y));
    }
    
    public void applyAngularImpulse(float impulse)
    {
        body.applyAngularImpulse(impulse);
    }
    
    public Body getEngineBody()
    {
        return body;
    }
    
    public void create(PhysicsWorld world)
    {
        body = world.getEngineWorld().createBody(bd);
        fixture = body.createFixture(fd);
    }
    
    public void destroy(PhysicsWorld world)
    {
        body.destroyFixture(fixture);
        world.getEngineWorld().destroyBody(body);
    }
    
    public void toDataOutputStream(DataOutputStream outputStream)
    {
        
    }
}
