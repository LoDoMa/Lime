package net.lodoma.lime.world.entity;

import net.lodoma.lime.util.Vector2;
import net.lodoma.lime.world.World;

public abstract class Entity
{
    private static final float MAX_LINEAR_VELOCITY = 5.0f;
    private static final float MAX_ANGULAR_VELOCITY = 720.0f;
    
    protected World world;
    
    /*** Entity this Entity is riding */
    protected Entity ridingOn;
    
    /*** Entity riding this Entity */
    protected Entity isRiding;
    
    protected Vector2 basePosition;
    protected Vector2 baseLinearVelocity;
    
    protected float baseAngle;
    protected float baseAngularVelocity;
    
    public final World getWorld()
    {
        return world;
    }

    public final void setWorld(World world)
    {
        this.world = world;
    }

    public final Entity getRidingOn()
    {
        return ridingOn;
    }

    public final void setRidingOn(Entity ridingOn)
    {
        this.ridingOn = ridingOn;
    }

    public final Entity getIsRiding()
    {
        return isRiding;
    }

    public final void setIsRiding(Entity isRiding)
    {
        this.isRiding = isRiding;
    }

    public final Vector2 getBasePosition()
    {
        return basePosition;
    }

    public final void setBasePosition(Vector2 basePosition)
    {
        this.basePosition = basePosition;
    }

    public final Vector2 getBaseLinearVelocity()
    {
        return baseLinearVelocity;
    }

    public final void setBaseLinearVelocity(Vector2 baseLinearVelocity)
    {
        this.baseLinearVelocity = baseLinearVelocity;
    }

    public final float getBaseAngle()
    {
        return baseAngle;
    }

    public final void setBaseAngle(float baseAngle)
    {
        this.baseAngle = baseAngle;
    }

    public final float getBaseAngularVelocity()
    {
        return baseAngularVelocity;
    }

    public final void setBaseAngularVelocity(float baseAngularVelocity)
    {
        this.baseAngularVelocity = baseAngularVelocity;
    }

    protected final void updateTransform(float timeDelta)
    {
        baseLinearVelocity.containLocal(-MAX_LINEAR_VELOCITY, MAX_LINEAR_VELOCITY, -MAX_LINEAR_VELOCITY, MAX_LINEAR_VELOCITY);
        basePosition.addLocal(baseLinearVelocity.mul(timeDelta));
        
        float bav = baseAngularVelocity;
        bav = (bav > MAX_ANGULAR_VELOCITY) ? MAX_ANGULAR_VELOCITY :
             ((bav < -MAX_ANGULAR_VELOCITY) ? -MAX_ANGULAR_VELOCITY : bav);
        baseAngularVelocity = bav;
        baseAngle += baseAngularVelocity * timeDelta;
    }
    
    public abstract void update(float timeDelta);
    public abstract void render();
}
