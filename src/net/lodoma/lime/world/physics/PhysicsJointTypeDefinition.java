package net.lodoma.lime.world.physics;

import org.jbox2d.dynamics.joints.JointDef;

public abstract class PhysicsJointTypeDefinition
{
    public abstract void validate() throws InvalidPhysicsJointException;
    public abstract JointDef newEngineInstance();
}
