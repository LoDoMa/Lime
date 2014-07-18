package net.lodoma.lime.physics.entity;

import java.util.ArrayList;
import java.util.List;

import net.lodoma.lime.mask.Mask;
import net.lodoma.lime.physics.PhysicsBody;
import net.lodoma.lime.physics.PhysicsJoint;
import net.lodoma.lime.physics.PhysicsWorld;
import net.lodoma.lime.script.LuaScript;
import net.lodoma.lime.world.entity.EntityWorld;

public class Entity
{
    private static long counterID = 0;
    
    boolean generatedID = false;
    long ID;
    
    long hash;
    String internalName;
    String visualName;
    String version;
    
    EntityWorld world;
    
    List<PhysicsBody> bodies;
    List<PhysicsJoint> joints;
    List<Mask> masks;
    List<String> properties;
    List<LuaScript> scripts;
    
    LuaEntity luaEntity;
    
    public Entity()
    {
        bodies = new ArrayList<PhysicsBody>();
        joints = new ArrayList<PhysicsJoint>();
        masks = new ArrayList<Mask>();
        properties = new ArrayList<String>();
        scripts = new ArrayList<LuaScript>();
        
        luaEntity = new LuaEntity(this);
    }
    
    public void generateID()
    {
        if(generatedID) return;
        generatedID = true;
        ID = counterID++;
    }
    
    public long getID()
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
    
    public LuaEntity getLuaEntity()
    {
        return luaEntity;
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
