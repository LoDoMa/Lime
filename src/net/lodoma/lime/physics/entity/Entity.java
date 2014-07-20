package net.lodoma.lime.physics.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.lodoma.lime.mask.Mask;
import net.lodoma.lime.physics.PhysicsBody;
import net.lodoma.lime.physics.PhysicsJoint;
import net.lodoma.lime.physics.PhysicsWorld;
import net.lodoma.lime.script.LuaScript;
import net.lodoma.lime.world.entity.EntityWorld;

public class Entity
{
    private static int counterID = 0;
    
    boolean created = false;
    
    boolean actor;
    
    boolean generatedID = false;
    int ID;
    
    long hash;
    String internalName;
    String visualName;
    String version;
    
    EntityWorld world;

    Map<Integer, PhysicsBody> bodies;
    Map<Integer, PhysicsJoint> joints;
    Map<Integer, Mask> masks;
    Map<Integer, String> properties;
    List<LuaScript> scripts;
    
    public Entity()
    {
        bodies = new HashMap<Integer, PhysicsBody>();
        joints = new HashMap<Integer, PhysicsJoint>();
        masks = new HashMap<Integer, Mask>();
        properties = new HashMap<Integer, String>();
        scripts = new ArrayList<LuaScript>();
    }
    
    public boolean isCreated()
    {
        return created;
    }
    
    public void setID(int id)
    {
        this.ID = id;
        generatedID = true;
    }
    
    public void setActor(boolean actor)
    {
        this.actor = actor;
    }
    
    public void generateID()
    {
        if(generatedID) return;
        generatedID = true;
        ID = counterID++;
    }
    
    public int getID()
    {
        return ID;
    }
    
    public long getHash()
    {
        return hash;
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
    
    public EntityWorld getEntityWorld()
    {
        return world;
    }
    
    public PhysicsBody getBody(int name)
    {
        return bodies.get(name);
    }
    
    public PhysicsJoint getJoint(int name)
    {
        return joints.get(name);
    }
    
    public Mask getMask(int name)
    {
        return masks.get(name);
    }
    
    public void create(PhysicsWorld world)
    {
        List<PhysicsBody> bodyList = new ArrayList<PhysicsBody>(bodies.values());
        List<PhysicsJoint> jointList = new ArrayList<PhysicsJoint>(joints.values());
        
        for(PhysicsBody body : bodyList)
            body.create(world);
        for(PhysicsJoint joint : jointList)
            joint.create(world);
        
        created = true;
    }
    
    public void destroy(PhysicsWorld world)
    {
        created = false;
        
        List<PhysicsBody> bodyList = new ArrayList<PhysicsBody>(bodies.values());
        List<PhysicsJoint> jointList = new ArrayList<PhysicsJoint>(joints.values());
        
        for(PhysicsBody body : bodyList)
            body.destroy(world);
        for(PhysicsJoint joint : jointList)
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
            script.call("Lime_FrameUpdate", timeDelta, actor, world.isServer() ? 0 : 1);
    }
    
    public void render()
    {
        List<Mask> maskList = new ArrayList<Mask>(masks.values());
        
        for(Mask mask : maskList)
            mask.call();
    }
}
