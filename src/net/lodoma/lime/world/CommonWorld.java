package net.lodoma.lime.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.lodoma.lime.common.PropertyPool;
import net.lodoma.lime.event.EventManager;
import net.lodoma.lime.physics.PhysicsWorld;
import net.lodoma.lime.physics.entity.Entity;
import net.lodoma.lime.physics.entity.EntityWorld;
import net.lodoma.lime.script.LuaEventListener;
import net.lodoma.lime.script.LuaScript;
import net.lodoma.lime.util.HashPool32;
import net.lodoma.lime.world.platform.Platform;

public abstract class CommonWorld implements EntityWorld
{
    protected String internalName;
    protected String visualName;
    protected String version;
    
    protected LuaScript script;
    
    protected PhysicsWorld physicsWorld;
    protected List<Platform> platforms;
    protected Map<Integer, Entity> entities;

    protected HashPool32<EventManager> emanPool;
    protected Map<Integer, LuaEventListener> luaListeners;
    
    public CommonWorld()
    {
        physicsWorld = new PhysicsWorld();
        platforms = new ArrayList<Platform>();
        entities = new HashMap<Integer, Entity>();
        
        luaListeners = new HashMap<Integer, LuaEventListener>();
    }
    
    public abstract PropertyPool getPropertyPool();
    
    @SuppressWarnings("unchecked")
    public void fetch()
    {
        emanPool = (HashPool32<EventManager>) getPropertyPool().getProperty("emanPool");
    }
    
    public PhysicsWorld getPhysicsWorld()
    {
        return physicsWorld; 
    }
    
    public void addPlatform(Platform platform)
    {
        platform.create(physicsWorld);
        platforms.add(platform);
    }
    
    public List<Platform> getPlatformList()
    {
        return platforms;
    }
    
    public void removePlatform(Platform platform)
    {
        platforms.remove(platform);
    }
    
    @Override
    public void addEntity(Entity entity)
    {
        entity.generateID();
        entity.create(physicsWorld);
        entities.put(entity.getID(), entity);
    }

    @Override
    public Entity getEntity(int id)
    {
        return entities.get(id);
    }
    
    public Set<Integer> getEntityIDSet()
    {
        return new HashSet<Integer>(entities.keySet());
    }

    @Override
    public void removeEntity(int id)
    {
        entities.remove(id);
    }
    
    public void addLuaEventListener(int hash)
    {
        luaListeners.put(hash, new LuaEventListener(hash, emanPool.get(hash), script));
    }
    
    public void releaseLuaEventListener(int hash)
    {
        luaListeners.get(hash).destroy();
        luaListeners.remove(hash);
    }
    
    public void clean()
    {
        for(Platform platform : platforms)
            platform.destroy(physicsWorld);
        platforms.clear();
        
        List<Entity> entityList = new ArrayList<Entity>(entities.values());
        for(Entity entity : entityList)
            entity.destroy(physicsWorld);
        entities.clear();
    }
    
    public void update(double timeDelta)
    {
        if(script != null)
            script.call("Lime_WorldUpdate");
        
        List<Entity> entityList = new ArrayList<Entity>(entities.values());
        for(Entity entity : entityList)
            entity.update(timeDelta);
        
        physicsWorld.update(timeDelta);
    }
}
