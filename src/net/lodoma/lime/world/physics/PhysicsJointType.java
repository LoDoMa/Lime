package net.lodoma.lime.world.physics;

import org.jbox2d.dynamics.joints.JointType;

public enum PhysicsJointType
{
    REVOLUTE(JointType.REVOLUTE);
    
    public final JointType engineType;
    
    private PhysicsJointType(JointType engineType)
    {
        this.engineType = engineType;
    }
}
