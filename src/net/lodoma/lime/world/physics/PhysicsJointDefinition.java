package net.lodoma.lime.world.physics;

import org.jbox2d.dynamics.joints.JointDef;

public class PhysicsJointDefinition
{
    public PhysicsComponent componentA;
    public PhysicsComponent componentB;
    public boolean collideConnected;
    
    public PhysicsJointType type;
    public PhysicsJointTypeDefinition typeDefinition;
    
    public JointDef engineJointDefinition;
    
    public void validate() throws InvalidPhysicsJointException
    {
        if (componentA == null)     throw new InvalidPhysicsJointException("invalid joint component A: null");
        if (componentB == null)     throw new InvalidPhysicsJointException("invalid joint component B: null");
        if (type == null)           throw new InvalidPhysicsJointException("invalid joint type: null");
        if (typeDefinition == null) throw new InvalidPhysicsJointException("invalid joint type definition: null");
        
        typeDefinition.validate();
    }
    
    public void create()
    {
        engineJointDefinition = typeDefinition.newEngineInstance();
        engineJointDefinition.bodyA = componentA.engineBody;
        engineJointDefinition.bodyB = componentB.engineBody;
        engineJointDefinition.collideConnected = collideConnected;
    }
}
