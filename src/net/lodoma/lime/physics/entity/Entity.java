package net.lodoma.lime.physics.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.lodoma.lime.mask.Mask;
import net.lodoma.lime.physics.PhysicsBody;
import net.lodoma.lime.physics.PhysicsJoint;

public class Entity
{
    private String internalName;
    private String visualName;
    private String version;
    
    private Map<String, PhysicsBody> physicsBodies;
    private Map<String, PhysicsJoint> physicsJoints;
    private Map<String, Mask> masks;
    private Map<String, String> properties;
    private List<String> scripts;
    
    public Entity()
    {
        physicsBodies = new HashMap<String, PhysicsBody>();
        physicsJoints = new HashMap<String, PhysicsJoint>();
        masks = new HashMap<String, Mask>();
        properties = new HashMap<String, String>();
        scripts = new ArrayList<String>();
    }
    
    public String getInternalName()
    {
        return internalName;
    }
    
    public String getVisualName()
    {
        return visualName;
    }
    
    public String getVersion()
    {
        return version;
    }
    
    public void setInternalName(String internalName)
    {
        this.internalName = internalName;
    }
    
    public void setVisualName(String visualName)
    {
        this.visualName = visualName;
    }
    
    public void setVersion(String version)
    {
        this.version = version;
    }
    
    public PhysicsBody getPhysicsBodyByName(String name)
    {
        return physicsBodies.get(name);
    }
    
    public void addPhysicsBody(String name, PhysicsBody body)
    {
        physicsBodies.put(name, body);
    }
    
    public PhysicsJoint getPhysicsJointByName(String name)
    {
        return physicsJoints.get(name);
    }
    
    public void addPhysicsJoint(String name, PhysicsJoint joint)
    {
        physicsJoints.put(name, joint);
    }
    
    public void addMask(String name, Mask mask)
    {
        masks.put(name, mask);
    }
    
    public void addProperty(String name, String type)
    {
        properties.put(name, type);
    }
    
    public void addScript(String script)
    {
        scripts.add(script);
    }
    
    public void update()
    {
        List<PhysicsBody> bodies = new ArrayList<PhysicsBody>(physicsBodies.values());
        for(PhysicsBody body : bodies)
            body.update();
    }
    
    public void render()
    {
        List<Mask> maskList = new ArrayList<Mask>(masks.values());
        for(Mask mask : maskList)
            mask.call();
    }
}
