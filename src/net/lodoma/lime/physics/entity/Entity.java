package net.lodoma.lime.physics.entity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.lodoma.lime.mask.Mask;
import net.lodoma.lime.physics.PhysicsBody;
import net.lodoma.lime.physics.PhysicsJoint;
import net.lodoma.lime.physics.PhysicsWorld;
import net.lodoma.lime.script.LuaScript;

public class Entity
{
    private String internalName;
    private String visualName;
    private String version;
    
    private List<PhysicsBody> bodies;
    private List<PhysicsJoint> joints;
    private List<Mask> masks;
    private List<String> properties;
    private List<LuaScript> scripts;
    
    public Entity()
    {
        bodies = new ArrayList<PhysicsBody>();
        joints = new ArrayList<PhysicsJoint>();
        masks = new ArrayList<Mask>();
        properties = new ArrayList<String>();
        scripts = new ArrayList<LuaScript>();
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
    
    public void addPhysicsBody(PhysicsBody body)
    {
        bodies.add(body);
    }
    
    public void addPhysicsJoint(PhysicsJoint joint)
    {
        joints.add(joint);
    }
    
    public void addMask(Mask mask)
    {
        masks.add(mask);
    }
    
    public void addProperty(String type)
    {
        properties.add(type);
    }
    
    public void addScript(String script)
    {
        try
        {
            scripts.add(new LuaScript(new File(script)));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void create(PhysicsWorld world)
    {
        for(PhysicsBody body : bodies)
            body.create(world);
        for(PhysicsJoint joint : joints)
            joint.create(world);
    }
    
    public void destroy(PhysicsWorld world)
    {
        for(PhysicsBody body : bodies)
            body.destroy(world);
        for(PhysicsJoint joint : joints)
            joint.destroy(world);
        
        for(LuaScript script : scripts)
            script.close();
        
        bodies.clear();
        joints.clear();
        masks.clear();
        properties.clear();
        scripts.clear();
    }
    
    public void update(double timeDelta)
    {
        for(LuaScript script : scripts)
            script.call("Lime_FrameUpdate", timeDelta);
        
        for(PhysicsBody body : bodies)
            body.update();
    }
    
    public void render()
    {
        for(Mask mask : masks)
            mask.call();
    }
}
