package net.lodoma.lime.physics;

import org.jbox2d.dynamics.joints.JointType;

public enum PhysicsJointType
{
    REVOLUTE(JointType.REVOLUTE),
    DISTANCE(JointType.DISTANCE),
    PRISMATIC(JointType.PRISMATIC),
    LINE(JointType.LINE),
    WELD(JointType.WELD),
    PULLEY(JointType.PULLEY),
    FRICTION(JointType.FRICTION),
    GEAR(JointType.GEAR),
    MOUSE(JointType.MOUSE);
    
    private JointType engineType;
    
    private PhysicsJointType(JointType engineType)
    {
        this.engineType = engineType;
    }
    
    public JointType getEngineType()
    {
        return engineType;
    }
}
