package net.lodoma.lime.physics;

import net.lodoma.lime.util.Vector2;

import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.JointDef;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

public class PhysicsJoint
{
    private PhysicsBody bodyA;
    private PhysicsBody bodyB;
    
    private JointDef jd;
    private Joint joint;
    
    public PhysicsJoint(PhysicsJointType type)
    {
        jd = type.jointDef();
        jd.type = type.getEngineType();
    }
    
    public void setBodyA(PhysicsBody bodyA)
    {
        this.bodyA = bodyA;
    }
    
    public void setBodyB(PhysicsBody bodyB)
    {
        this.bodyB = bodyB;
    }
    
    public void setCollisionEnabled(boolean collideConnected)
    {
        jd.collideConnected = collideConnected;
    }
    
    public void setAnchorA(Vector2 anchorA)
    {
        ((RevoluteJointDef) jd).localAnchorA.set(anchorA.x, anchorA.y);
    }
    
    public void setAnchorB(Vector2 anchorB)
    {
        ((RevoluteJointDef) jd).localAnchorB.set(anchorB.x, anchorB.y);
    }
    
    public void setMotorEnabled(boolean motor)
    {
        ((RevoluteJointDef) jd).enableMotor = motor;
    }
    
    public void setMotorSpeed(float motorSpeed)
    {
        ((RevoluteJointDef) jd).motorSpeed = motorSpeed;
    }
    
    public void setMaxMotorTorque(float maxMotorTorque)
    {
        ((RevoluteJointDef) jd).maxMotorTorque = maxMotorTorque;
    }
    
    public void setLimitEnabled(boolean limit)
    {
        ((RevoluteJointDef) jd).enableLimit = true;
    }
    
    public void setLimits(float lower, float upper)
    {
        ((RevoluteJointDef) jd).lowerAngle = lower;
        ((RevoluteJointDef) jd).upperAngle = upper;
    }
    
    public void setAngle(float angle)
    {
        ((RevoluteJointDef) jd).referenceAngle = angle;
    }
    
    public Joint getEngineJoint()
    {
        return joint;
    }
    
    public void create(PhysicsWorld world)
    {
        jd.bodyA = bodyA.getEngineBody();
        jd.bodyB = bodyB.getEngineBody();
        World engineWorld = world.getEngineWorld();
        joint = engineWorld.createJoint(jd);
    }
    
    public void destroy(PhysicsWorld world)
    {
        World engineWorld = world.getEngineWorld();
        engineWorld.destroyJoint(joint);
    }
}
