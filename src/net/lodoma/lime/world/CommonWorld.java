package net.lodoma.lime.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.lodoma.lime.common.PropertyPool;
import net.lodoma.lime.physics.PhysicsWorld;
import net.lodoma.lime.physics.entity.Entity;
import net.lodoma.lime.physics.entity.EntityWorld;
import net.lodoma.lime.script.LuaScript;
import net.lodoma.lime.world.platform.Platform;

public abstract class CommonWorld implements EntityWorld
{
    protected String name;
    protected String version;
    
    protected LuaScript script;
    
    protected PhysicsWorld physicsWorld;
    protected List<Platform> platforms;
    protected Map<Integer, Entity> entities;
    
    public CommonWorld()
    {
        physicsWorld = new PhysicsWorld();
        platforms = new ArrayList<Platform>();
        entities = new HashMap<Integer, Entity>();
    }
    
    public abstract PropertyPool getPropertyPool();
    
    public PhysicsWorld getPhysicsWorld()
    {
        return physicsWorld; 
    }
    
    public synchronized void addPlatform(Platform platform)
    {
        platforms.add(platform);
    }
    
    public synchronized List<Platform> getPlatformList()
    {
        return new ArrayList<Platform>(platforms);
    }
    
    public synchronized void removePlatform(Platform platform)
    {
        platforms.remove(platform);
    }
    
    @Override
    public synchronized void addEntity(Entity entity)
    {
        entity.generateID();
        entities.put(entity.getID(), entity);
    }

    @Override
    public synchronized Entity getEntity(int id)
    {
        return entities.get(id);
    }
    
    public synchronized Set<Integer> getEntityIDSet()
    {
        return new HashSet<Integer>(entities.keySet());
    }
    
    public synchronized List<Entity> getEntityList()
    {
        return new ArrayList<Entity>(entities.values());
    }

    @Override
    public synchronized void removeEntity(int id)
    {
        entities.remove(id);
    }
    
    public void clean()
    {
        List<Platform> platformList = getPlatformList();
        for(Platform platform : platformList)
        {
            platform.destroy(physicsWorld);
            removePlatform(platform);
        }
        
        List<Entity> entityList = getEntityList();
        for(Entity entity : entityList)
        {
            entity.destroy(physicsWorld);
            removeEntity(entity.getID());
        }
    }
    
    public void update(double timeDelta)
    {
        if(script != null)
            script.call("Lime_WorldUpdate", null);
        
        List<Entity> entityList = getEntityList();
        for(Entity entity : entityList)
            entity.update(timeDelta);
        
        physicsWorld.update(timeDelta);
    }
}
