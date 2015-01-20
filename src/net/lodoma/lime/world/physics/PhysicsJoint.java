package net.lodoma.lime.world.physics;

import org.jbox2d.dynamics.joints.Joint;

import net.lodoma.lime.util.Identifiable;

public class PhysicsJoint implements Identifiable<Integer>
{
    public int identifier;
    
    public PhysicsWorld world;
    
    public Joint engineJoint;
    
    public PhysicsJoint(PhysicsJointDefinition definition, PhysicsWorld world)
    {
        this.world = world;
        
        engineJoint = world.engineWorld.createJoint(definition.engineJointDefinition);
    }
    
    @Override
    public Integer getIdentifier()
    {
        return identifier;
    }
    
    @Override
    public void setIdentifier(Integer identifier)
    {
        this.identifier = identifier;
    }
    
    public void destroy()
    {
        world.engineWorld.destroyJoint(engineJoint);
    }
}
