package net.lodoma.lime.physics;

import org.jbox2d.dynamics.joints.JointDef;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

public class PhysicsJointDescription
{
    int bodyA;
    int bodyB;
    JointDef jointDef;
    
    public PhysicsJointDescription(PhysicsJointData data)
    {
        bodyA = data.bodyA;
        bodyB = data.bodyB;
        
        jointDef = data.jointType.jointDef();
        jointDef.type = data.jointType.getEngineType();
        jointDef.collideConnected = data.collide;
        
        switch(data.jointType)
        {
        case REVOLUTE:
            ((RevoluteJointDef) jointDef).localAnchorA = data.anchorA.toVec2();
            ((RevoluteJointDef) jointDef).localAnchorB = data.anchorB.toVec2();
            ((RevoluteJointDef) jointDef).referenceAngle = data.angle;
            
            ((RevoluteJointDef) jointDef).enableMotor = data.motorEnabled;
            ((RevoluteJointDef) jointDef).motorSpeed = data.motorSpeed;
            ((RevoluteJointDef) jointDef).maxMotorTorque = data.maxMotorTorque;

            ((RevoluteJointDef) jointDef).enableLimit = data.limitEnabled;
            ((RevoluteJointDef) jointDef).lowerAngle = data.lowLimit;
            ((RevoluteJointDef) jointDef).upperAngle = data.highLimit;
            break;
        default:
            // TODO: handle other types
            break;
        }
    }
}
