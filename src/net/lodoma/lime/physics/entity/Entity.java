package net.lodoma.lime.physics.entity;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.lodoma.lime.mask.Mask;
import net.lodoma.lime.physics.PhysicsBody;
import net.lodoma.lime.physics.PhysicsJoint;
import net.lodoma.lime.physics.PhysicsWorld;
import net.lodoma.lime.script.LuaScript;
import net.lodoma.lime.world.entity.EntityWorld;

public class Entity
{
    private static long counterID = 0;
    
    boolean actor;
    
    boolean generatedID = false;
    long ID;
    
    long hash;
    String internalName;
    String visualName;
    String version;
    
    EntityWorld world;
    
    Map<String, PhysicsBody> bodies;
    Map<String, PhysicsJoint> joints;
    Map<String, Mask> masks;
    Map<String, String> properties;
    List<LuaScript> scripts;
    
    public Entity()
    {
        bodies = new HashMap<String, PhysicsBody>();
        joints = new HashMap<String, PhysicsJoint>();
        masks = new HashMap<String, Mask>();
        properties = new HashMap<String, String>();
        scripts = new ArrayList<LuaScript>();
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
    
    public EntityWorld getEntityWorld()
    {
        return world;
    }
    
    public PhysicsBody getBody(String name)
    {
        return bodies.get(name);
    }
    
    public PhysicsJoint getJoint(String name)
    {
        return joints.get(name);
    }
    
    public Mask getMask(String name)
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
    }
    
    public void destroy(PhysicsWorld world)
    {
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
    
    public void toDataOutputStream(DataOutputStream outputStream) throws IOException
    {
        outputStream.writeBoolean(actor);
        outputStream.writeLong(ID);
        outputStream.writeChars(internalName); outputStream.writeChar(0);
        outputStream.writeChars(visualName); outputStream.writeChar(0);
        outputStream.writeChars(version); outputStream.writeChar(0);
        
        outputStream.writeInt(bodies.size());
        Set<Entry<String, PhysicsBody>> bodyEntries = bodies.entrySet();
        for(Entry<String, PhysicsBody> entry : bodyEntries)
        {
            outputStream.writeChars(entry.getKey()); outputStream.writeChar(0);
            entry.getValue().toDataOutputStream(outputStream);
        }
        
        // TODO finish
    }
}
