package net.lodoma.lime.physics;

import java.nio.ByteBuffer;

import net.lodoma.lime.util.Vector2;

public class PhysicsJoint
{
    private PhysicsBody bodyA;
    private PhysicsBody bodyB;
    private Vector2 anchorA;
    private Vector2 anchorB;
    
    private ByteBuffer buffer;
    
    public PhysicsJoint()
    {
        
    }
    
    private PhysicsBody getBodyByID(long bodyID)
    {
        return null;
    }
    
    public void setBodyA(long bodyA_ID, Vector2 anchorA)
    {
        this.bodyA = getBodyByID(bodyA_ID);
        this.anchorA = anchorA;
    }
    
    public void setBodyB(long bodyB_ID, Vector2 anchorB)
    {
        this.bodyB = getBodyByID(bodyB_ID);
        this.anchorB = anchorB;
    }
    
    
}
