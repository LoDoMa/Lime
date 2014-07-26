package net.lodoma.lime.physics.entity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.lodoma.lime.event.EventManager;
import net.lodoma.lime.mask.Mask;
import net.lodoma.lime.physics.PhysicsBody;
import net.lodoma.lime.physics.PhysicsJoint;
import net.lodoma.lime.physics.PhysicsWorld;
import net.lodoma.lime.script.LuaScript;
import net.lodoma.lime.util.HashPool32;
import net.lodoma.lime.util.Vector2;

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
    LuaScript script;
    
    private HashPool32<EventManager> emanPool;
    private Map<Integer, EntityEventListener> listeners;
    
    public Entity(HashPool32<EventManager> emanPool)
    {
        bodies = new HashMap<Integer, PhysicsBody>();
        joints = new HashMap<Integer, PhysicsJoint>();
        masks = new HashMap<Integer, Mask>();
        properties = new HashMap<Integer, String>();
        
        this.emanPool = emanPool;
        listeners = new HashMap<Integer, EntityEventListener>();
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
    
    public void addEventListener(int hash)
    {
        listeners.put(hash, new EntityEventListener(hash, emanPool.get(hash), script));
    }
    
    public void releaseEventListener(int hash)
    {
        listeners.remove(hash);
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
        
        script.close();
        
        bodies.clear();
        joints.clear();
        masks.clear();
        properties.clear();
    }
    
    public void update(double timeDelta)
    {
        script.call("Lime_FrameUpdate", timeDelta, actor, world.isServer() ? 0 : 1);
    }
    
    public void render()
    {
        List<Mask> maskList = new ArrayList<Mask>(masks.values());
        
        for(Mask mask : maskList)
            mask.call();
    }
    
    public void receiveCreation(DataInputStream inputStream) throws IOException
    {
        List<Integer> bodyKeys = new ArrayList<Integer>(bodies.keySet());
        Collections.sort(bodyKeys);
        
        for(Integer i : bodyKeys)
        {
            PhysicsBody body = bodies.get(i);
            body.setPosition(new Vector2(inputStream.readFloat(), inputStream.readFloat()));
            body.setAngle(inputStream.readFloat());
        }
    }
    
    public void sendCreation(DataOutputStream outputStream) throws IOException
    {
        List<Integer> bodyKeys = new ArrayList<Integer>(bodies.keySet());
        Collections.sort(bodyKeys);
        
        for(Integer i : bodyKeys)
        {
            PhysicsBody body = bodies.get(i);
            Vector2 pos = body.getPosition();
            outputStream.writeFloat(pos.x);
            outputStream.writeFloat(pos.y);
            outputStream.writeFloat(body.getAngle());
        }
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
