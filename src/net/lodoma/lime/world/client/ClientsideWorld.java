package net.lodoma.lime.world.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.physics.PhysicsWorld;
import net.lodoma.lime.physics.entity.Entity;
import net.lodoma.lime.physics.entity.EntityWorld;
import net.lodoma.lime.world.platform.Platform;

public class ClientsideWorld implements EntityWorld
{
    private PhysicsWorld physicsWorld;
    private List<Platform> platforms;
    private Map<Integer, Entity> entities;
    
    public ClientsideWorld(Client client)
    {
        physicsWorld = new PhysicsWorld();
        platforms = new ArrayList<Platform>();
        entities = new HashMap<Integer, Entity>();
    }
    
    @Override
    public boolean isServer()
    {
        return false;
    }
    
    public void clean()
    {
        for(Platform platform : platforms)
            platform.destroy(physicsWorld);
        platforms.clear();
        
        List<Entity> entityList = new ArrayList<Entity>(entities.values());
        for(Entity entity : entityList)
            if(entity.isCreated())
                entity.destroy(physicsWorld);
        entities.clear();
    }
    
    public void fetch()
    {
        
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
    
    public void update(double timeDelta)
    {
        List<Entity> entityList = new ArrayList<Entity>(entities.values());
        for(Entity entity : entityList)
            if(entity.isCreated())
                entity.update(timeDelta);
        
        physicsWorld.update(timeDelta);
    }
    
    public void render()
    {
        for(Platform platform : platforms)
            platform.render();
        
        List<Entity> entityList = new ArrayList<Entity>(entities.values());
        for(Entity entity : entityList)
            entity.render();
    }
}
