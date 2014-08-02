package net.lodoma.lime.physics.entity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.lodoma.lime.common.PropertyPool;
import net.lodoma.lime.mask.Mask;
import net.lodoma.lime.physics.PhysicsBody;
import net.lodoma.lime.physics.PhysicsJoint;
import net.lodoma.lime.physics.PhysicsWorld;
import net.lodoma.lime.script.LuaScript;

public class Entity
{
    private static int counterID = 0;
    
    boolean generatedID = false;
    int ID;
    
    int hash;
    String internalName;
    String visualName;
    String version;
    
    EntityWorld world;
    PropertyPool propertyPool;

    Map<Integer, PhysicsBody> bodies;
    Map<Integer, PhysicsJoint> joints;
    Map<Integer, Mask> masks;
    Map<Integer, String> properties;
    LuaScript script;
    File scriptFile;
    
    public Entity()
    {
        bodies = new HashMap<Integer, PhysicsBody>();
        joints = new HashMap<Integer, PhysicsJoint>();
        masks = new HashMap<Integer, Mask>();
        properties = new HashMap<Integer, String>();
    }
    
    public Entity newCopy() throws IOException
    {
        Entity copy = new Entity();
        
        copy.hash = hash;
        copy.internalName = internalName;
        copy.visualName = visualName;
        copy.version = version;
        
        copy.world = world;
        copy.propertyPool = propertyPool;

        Set<Integer> bodyHashes = bodies.keySet();
        Set<Integer> jointHashes = joints.keySet();
        Set<Integer> maskHashes = masks.keySet();
        Set<Integer> propertyHashes = properties.keySet();

        for(Integer hash : bodyHashes)
            copy.bodies.put(hash, bodies.get(hash).newCopy());
        for(Integer hash : jointHashes)
            copy.joints.put(hash, joints.get(hash).newCopy(copy));
        for(Integer hash : maskHashes)
            copy.masks.put(hash, masks.get(hash).newCopy());
        for(Integer hash : propertyHashes)
            copy.properties.put(hash, new String(properties.get(hash)));

        copy.script = new LuaScript();
        copy.script.setGlobal("ENTITY", copy);
        copy.script.setGlobal("SCRIPT", copy.script);
        copy.script.require("script/strict/entity");
        copy.script.require("script/strict/sandbox");
        copy.scriptFile = scriptFile;
        copy.script.load(copy.scriptFile);
        
        return copy;
    }
    
    public void setID(int id)
    {
        this.ID = id;
        generatedID = true;
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
    
    public int getHash()
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
    
    public PropertyPool getPropertyPool()
    {
        return propertyPool;
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
    }
    
    public void destroy(PhysicsWorld world)
    {
        List<PhysicsBody> bodyList = new ArrayList<PhysicsBody>(bodies.values());
        List<PhysicsJoint> jointList = new ArrayList<PhysicsJoint>(joints.values());
        
        for(PhysicsBody body : bodyList)
            body.destroy(world);
        for(PhysicsJoint joint : jointList)
            joint.destroy(world);
        
        script.close();
        
        bodies.clear();
        joints.clear();
        masks.clear();
        properties.clear();
    }
    
    public void update(double timeDelta)
    {
        boolean actor = propertyPool.hasProperty("actor") && ((Integer) propertyPool.getProperty("actor")) == ID;
        script.call("Lime_FrameUpdate", timeDelta, actor);
    }
    
    public void render()
    {
        List<Mask> maskList = new ArrayList<Mask>(masks.values());
        
        for(Mask mask : maskList)
            mask.call();
    }
    
    public void receiveCorrection(DataInputStream inputStream) throws IOException
    {
        int count = bodies.size();
        for(int i = 0; i < count; i++)
        {
            int hash = inputStream.readInt();
            bodies.get(hash).receiveCorrection(inputStream);
        }
    }
    
    public void sendCorrection(DataOutputStream outputStream) throws IOException
    {
        Set<Integer> hashes = new HashSet<Integer>(bodies.keySet());
        for(Integer hash : hashes)
        {
            outputStream.writeInt(hash);
            bodies.get(hash).sendCorrection(outputStream);
        }
    }
}
