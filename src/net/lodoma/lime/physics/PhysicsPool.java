package net.lodoma.lime.physics;

import java.util.HashMap;
import java.util.Map;

public class PhysicsPool
{
    private Map<Long, PhysicsBody> bodies;
    private Map<Long, PhysicsJoint> joints;
    
    public PhysicsPool()
    {
        bodies = new HashMap<Long, PhysicsBody>();
        joints = new HashMap<Long, PhysicsJoint>();
    }
    
    public void addBody(PhysicsBody body)
    {
        bodies.put(body.getID(), body);
    }
    
    public PhysicsBody getBody(long bodyID)
    {
        return bodies.get(bodyID);
    }
    
    public void addJoint(PhysicsJoint joint)
    {
        joints.put(joint.getID(), joint);
    }
    
    public PhysicsJoint getJoint(long jointID)
    {
        return joints.get(jointID);
    }
}
