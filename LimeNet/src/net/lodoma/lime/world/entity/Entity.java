package net.lodoma.lime.world.entity;

import net.lodoma.lime.util.Vector2;
import net.lodoma.lime.world.World;

public abstract class Entity
{
    private static final float MAX_LINEAR_VELOCITY = 5.0f;
    private static final float MAX_ANGULAR_VELOCITY = 720.0f;
    
    private World world;
    
    /*** Entity this Entity is riding */
    private Entity ridingOn;
    
    /*** Entity riding this Entity */
    private Entity isRiding;
    
    private Vector2 basePosition;
    private Vector2 baseLinearVelocity;
    
    private float baseAngle;
    private float baseAngularVelocity;
    
    protected void updateTransform(float timeDelta)
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
