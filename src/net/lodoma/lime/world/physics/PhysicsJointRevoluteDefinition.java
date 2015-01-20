package net.lodoma.lime.world.physics;

import net.lodoma.lime.util.Vector2;

import org.jbox2d.dynamics.joints.JointDef;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

public class PhysicsJointRevoluteDefinition extends PhysicsJointTypeDefinition
{
    public Vector2 localAnchorA = new Vector2(0.0f, 0.0f);
    public Vector2 localAnchorB = new Vector2(0.0f, 0.0f);
    public boolean limitEnabled;
    public float lowerAngleLimit;
    public float upperAngleLimit;
    public boolean motorEnabled;
    public float motorSpeed;
    public float maxMotorTorque;
    
    @Override
    public void validate() throws InvalidPhysicsJointException
    {
        if (localAnchorA == null) throw new InvalidPhysicsJointException("invalid revolute joint local anchor A: null");
        if (localAnchorB == null) throw new InvalidPhysicsJointException("invalid revolute joint local anchor B: null");
    }
    
    @Override
    public JointDef newEngineInstance()
    {
        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.localAnchorA = localAnchorA.toVec2();
        jointDef.localAnchorB = localAnchorB.toVec2();
        jointDef.enableLimit = limitEnabled;
        if (limitEnabled)
        {
            jointDef.lowerAngle = lowerAngleLimit;
            jointDef.upperAngle = upperAngleLimit;
        }
        jointDef.enableMotor = motorEnabled;
        if (motorEnabled)
        {
            jointDef.motorSpeed = motorSpeed;
            jointDef.maxMotorTorque = maxMotorTorque;
        }
        return jointDef;
    }
}
