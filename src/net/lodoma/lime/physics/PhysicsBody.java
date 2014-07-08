package net.lodoma.lime.physics;

import net.lodoma.lime.util.Vector2;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
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
    
    public void setPosition(Vector2 pos)
    {
        bd.position.set(pos.x, pos.y);
    }
    
    public void setBodyType(PhysicsBodyType type)
    {
        bd.type = type.getEngineValue();
    }
    
    private void setShape(Shape shape)
    {
        this.shape = shape;
        fd.shape = this.shape;
    }
    
    public void setCircleShape(float radius)
    {
        CircleShape circleShape = new CircleShape();
        circleShape.m_radius = radius;
        setShape(circleShape);
    }
    
    public void setPolygonShape(Vector2... vertices)
    {
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.set(Vector2.toVec2Array(vertices), vertices.length);
        setShape(polygonShape);
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
}
