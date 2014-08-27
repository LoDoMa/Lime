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
import net.lodoma.lime.localization.Language;
import net.lodoma.lime.mask.Mask;
import net.lodoma.lime.physics.PhysicsBody;
import net.lodoma.lime.physics.PhysicsJoint;
import net.lodoma.lime.physics.PhysicsWorld;
import net.lodoma.lime.script.LuaScript;

public class Entity
{
    private int ID;
    
    private int hash;
    private String name;
    private String version;
    
    private EntityWorld world;
    private PropertyPool propertyPool;

    private Map<Integer, PhysicsBody> bodies;
    private Map<Integer, PhysicsJoint> joints;
    private Map<Integer, Mask> masks;
    private Map<Integer, String> properties;
    private LuaScript script;
    private File scriptFile;
    
    public Entity(EntityWorld entityWorld, PhysicsWorld physicsWorld, PropertyPool propertyPool, EntityData data, int id) throws IOException
    {
        hash = data.nameHash;
        name = data.name;
        version = data.version;
        
        world = entityWorld;
        this.propertyPool = propertyPool;

        bodies = new HashMap<Integer, PhysicsBody>();
        joints = new HashMap<Integer, PhysicsJoint>();
        masks = new HashMap<Integer, Mask>();
        properties = new HashMap<Integer, String>();
        
        Set<Integer> bodyHashes = data.bodies.keySet();
        Set<Integer> jointHashes = data.joints.keySet();
        Set<Integer> maskHashes = data.masks.keySet();

        for(int bodyHash : bodyHashes)
        {
            PhysicsBody body = new PhysicsBody(physicsWorld, data.bodies.get(bodyHash));
            bodies.put(bodyHash, body);
        }
        
        for(int jointHash : jointHashes)
        {
            PhysicsJoint joint = new PhysicsJoint(this, physicsWorld, data.joints.get(jointHash));
            joints.put(jointHash, joint);
        }
        
        for(int maskHash : maskHashes)
        {
            Mask mask = data.masks.get(maskHash).newCopy();
            masks.put(maskHash, mask);
        }
        
        ID = id;

        script = new LuaScript();
        script.setGlobal("ENTITY", this);
        script.setGlobal("SCRIPT", script);
        script.require("script/strict/lime");
        script.require("script/strict/entity");
        script.require("script/strict/sandbox");
        scriptFile = new File(data.script);
        script.load(scriptFile);
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
    
    public int getID()
    {
        return ID;
    }
    
    public int getHash()
    {
        return hash;
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getLocalizedName(Language language)
    {
        return language.getLocalizedName((String) (script.call("Lime_GetUnlocalizedName", 1, null)[0]));
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
    
    public void update(double timeDelta)
    {
        boolean actor = propertyPool.hasProperty("actor") && ((Integer) propertyPool.getProperty("actor")) == ID;
        script.call("Lime_FrameUpdate", new Object[] { timeDelta, actor });
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
