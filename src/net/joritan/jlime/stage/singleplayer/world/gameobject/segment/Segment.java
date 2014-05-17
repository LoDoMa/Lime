package net.joritan.jlime.stage.singleplayer.world.gameobject.segment;

import net.joritan.jlime.util.Vector2;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

public abstract class Segment
{
    protected Body body;

    public Body getBody()
    {
        return body;
    }

    public Vector2 getLinearVelocity()
    {
        return new Vector2(body.getLinearVelocity());
    }

    public void setLinearVelocity(Vector2 velocity)
    {
        body.setLinearVelocity(new Vec2(velocity.x, velocity.y));
    }

    public float getAngularVelocity()
    {
        return body.getAngularVelocity();
    }

    public void setAngularVelocity(float velocity)
    {
        body.setAngularVelocity(velocity);
    }

    public float getLinearDamping(float damping)
    {
        return body.getLinearDamping();
    }

    public void setLinearDamping(float damping)
    {
        body.setLinearDamping(damping);
    }

    public float getAngularDamping(float damping)
    {
        return body.getAngularDamping();
    }

    public void setAngularDamping(float damping)
    {
        body.setAngularDamping(damping);
    }

    public void setFixedRotation(boolean flag)
    {
        body.setFixedRotation(flag);
    }

    public Vector2 getPosition()
    {
        return new Vector2(body.getPosition());
    }

    public void setPosition(Vector2 position)
    {
        setTransform(position, getAngle());
    }

    public float getAngle()
    {
        return body.getAngle();
    }

    public void setAngle(float angle)
    {
        setTransform(getPosition(), angle);
    }

    public void setTransform(Vector2 vector2, float angle)
    {
        body.setTransform(new Vec2(vector2.x, vector2.y), angle);
    }

    public abstract void update(float timeDelta);
    public abstract void render();
}
