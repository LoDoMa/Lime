package net.lodoma.lime.physics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhysicsPool
{
    protected List<PoolListener> listeners;
    protected Map<Long, PhysicsBody> bodies;
    protected Map<Long, PhysicsJoint> joints;
    
    public PhysicsPool()
    {
        listeners = new ArrayList<PoolListener>();
        bodies = new HashMap<Long, PhysicsBody>();
        joints = new HashMap<Long, PhysicsJoint>();
    }
    
    public void fetch()
    {
        
    }
    
    public void addListener(PoolListener listener)
    {
        listeners.add(listener);
    }
    
    public void addBody(PhysicsBody body)
    {
        bodies.put(body.getID(), body);
        for(PoolListener listener : listeners)
            listener.onNewBody(this, body);
    }
    
    public PhysicsBody getBody(long bodyID)
    {
        return bodies.get(bodyID);
    }
    
    public List<PhysicsBody> getBodies()
    {
        return new ArrayList<PhysicsBody>(bodies.values());
    }
    
    public void addJoint(PhysicsJoint joint)
    {
        joints.put(joint.getID(), joint);
        for(PoolListener listener : listeners)
            listener.onNewJoint(this, joint);
    }
    
    public PhysicsJoint getJoint(long jointID)
    {
        return joints.get(jointID);
    }
    
    public List<PhysicsJoint> getJoints()
    {
        return new ArrayList<PhysicsJoint>(joints.values());
    }
}
