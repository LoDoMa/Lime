package net.lodoma.lime.physics.entity;

import java.util.HashMap;
import java.util.Map;

import net.lodoma.lime.mask.Mask;
import net.lodoma.lime.physics.PhysicsBodyDescription;
import net.lodoma.lime.physics.PhysicsJointDescription;

public class EntityData
{
    public int nameHash;
    public String internalName;
    public String visualName;
    public String version;
    
    public Map<Integer, PhysicsBodyDescription> bodies;
    public Map<Integer, PhysicsJointDescription> joints;
    public Map<Integer, Mask> masks;
    public String script;
    
    public EntityData()
    {
        bodies = new HashMap<Integer, PhysicsBodyDescription>();
        joints = new HashMap<Integer, PhysicsJointDescription>();
        masks = new HashMap<Integer, Mask>();
    }
}
