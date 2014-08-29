package net.lodoma.lime.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.lodoma.lime.common.NetworkSide;
import net.lodoma.lime.common.PropertyPool;
import net.lodoma.lime.physics.PhysicsWorld;
import net.lodoma.lime.physics.entity.Entity;
import net.lodoma.lime.physics.entity.EntityPool;
import net.lodoma.lime.script.LuaScript;
import net.lodoma.lime.world.platform.Platform;

public abstract class CommonWorld
{
    protected String name;
    protected String version;
    
    protected LuaScript script;
    
    protected PhysicsWorld physicsWorld;
    protected List<Platform> platforms;
    
    protected EntityPool entityPool;
    
    public CommonWorld()
    {
        physicsWorld = new PhysicsWorld();
        platforms = new ArrayList<Platform>();
        
        entityPool = new EntityPool();
    }
    
    public abstract NetworkSide getNetworkSide();
    
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
    
    public void addEntity(Entity entity)
    {
        entityPool.add(entity);
    }
    
    public Entity getEntity(int id)
    {
        return entityPool.get(id);
    }
    
    public void removeEntity(int id)
    {
        entityPool.remove(id);
    }
    
    public Set<Integer> getEntityIDSet()
    {
        return entityPool.getIdentifierSet();
    }
    
    public List<Entity> getEntityList()
    {
        return entityPool.getObjectList();
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
            entity.destroy();
            entityPool.remove(entity.getIdentifier());
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
