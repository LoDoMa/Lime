package net.lodoma.lime.physics;

import net.lodoma.lime.physics.entity.Entity;

import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.Joint;

public class PhysicsJoint
{
    private Joint joint;
    
    public PhysicsJoint(Entity entity, PhysicsWorld world, PhysicsJointDescription description)
    {
        description.jointDef.bodyA = entity.getBody(description.bodyA).getEngineBody();
        description.jointDef.bodyB = entity.getBody(description.bodyB).getEngineBody();
        
        joint = world.getEngineWorld().createJoint(description.jointDef);
    }
    
    public void destroy(PhysicsWorld world)
    {
        World engineWorld = world.getEngineWorld();
        engineWorld.destroyJoint(joint);
    }
    
    public Joint getEngineJoint()
    {
        return joint;
    }
}
