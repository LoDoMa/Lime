package net.lodoma.lime.physics;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.lodoma.lime.util.Vector2;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

public class PhysicsBody
{
    private Fixture fixture;
    private Body body;
    
    public PhysicsBody(PhysicsWorld world, PhysicsBodyDescription description)
    {
        body = world.getEngineWorld().createBody(description.bodyDef);
        fixture = body.createFixture(description.fixtureDef);
    }
    
    public void destroy(PhysicsWorld world)
    {
        body.destroyFixture(fixture);
        world.getEngineWorld().destroyBody(body);
    }
    
    public Body getEngineBody()
    {
        return body;
    }
    
    public Vector2 getPosition()
    {
        return new Vector2(body.getPosition());
    }
    
    public void setPosition(Vector2 pos)
    {
        body.setTransform(Vector2.toVec2(pos), body.getAngle());
    }
    
    public float getAngle()
    {
        return body.getAngle();
    }
    
    public void setAngle(float angle)
    {
        body.setTransform(body.getPosition(), angle);
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
