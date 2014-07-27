package net.lodoma.lime.physics;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
        body.setTransform(Vector2.toVec2(pos), body.getAngle());
    }
    
    public void setDefPosition(Vector2 pos)
    {
        bd.position = Vector2.toVec2(pos);
    }
    
    public void setAngle(float angle)
    {
        body.setTransform(body.getPosition(), angle);
    }
    
    public void setDefAngle(float angle)
    {
        bd.angle = angle;
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
    
    public void applyForce(Vector2 force, Vector2 point)
    {
        body.applyForce(Vector2.toVec2(force), Vector2.toVec2(point));
    }
    
    public void applyLinearImpulse(Vector2 impulse, Vector2 point)
    {
        body.applyLinearImpulse(Vector2.toVec2(impulse), Vector2.toVec2(point));
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
    
    public void receiveCorrection(DataInputStream inputStream) throws IOException
    {
        float posX, posY;
        float angle;
        float linVelX, linVelY;
        float angVel;

        posX = inputStream.readFloat();
        posY = inputStream.readFloat();
        angle = inputStream.readFloat();
        linVelX = inputStream.readFloat();
        linVelY = inputStream.readFloat();
        angVel = inputStream.readFloat();
        
        body.setTransform(new Vec2(posX, posY), angle);
        body.setLinearVelocity(new Vec2(linVelX, linVelY));
        body.setAngularVelocity(angVel);
    }
    
    public void sendCorrection(DataOutputStream outputStream) throws IOException
    {
        Vec2 position = body.getPosition();
        float angle = body.getAngle();
        Vec2 linearVelocity = body.getLinearVelocity();
        float angularVelocity = body.getAngularVelocity();
        
        outputStream.writeFloat(position.x);
        outputStream.writeFloat(position.y);
        outputStream.writeFloat(angle);
        outputStream.writeFloat(linearVelocity.x);
        outputStream.writeFloat(linearVelocity.y);
        outputStream.writeFloat(angularVelocity);
    }
}
