package net.lodoma.lime.world.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.lodoma.lime.physics.PhysicsWorld;
import net.lodoma.lime.physics.entity.Entity;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.world.entity.EntityWorld;

public class ServersideWorld implements EntityWorld
{
    private PhysicsWorld physicsWorld;
    private Map<Integer, Entity> entities;
    private List<Integer> entitiesToCreate;
    
    public ServersideWorld(Server server)
    {
        physicsWorld = new PhysicsWorld();
        entities = new HashMap<Integer, Entity>();
        entitiesToCreate = new ArrayList<Integer>();
    }
    
    @Override
    public boolean isServer()
    {
        return true;
    }
    
    public void fetch()
    {
        
    }
    
    public void clean()
    {
        List<Entity> entityList = new ArrayList<Entity>(entities.values());
        for(Entity entity : entityList)
            if(entity.isCreated())
                entity.destroy(physicsWorld);
        entities.clear();
    }
    
    public PhysicsWorld getPhysicsWorld()
    {
        return physicsWorld;
    }
    
    public void createEntity(int id)
    {
        entitiesToCreate.add(id);
    }
    
    @Override
    public void addEntity(Entity entity)
    {
        entity.generateID();
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
        List<Integer> createdEntities = new ArrayList<Integer>();
        for(int entityID : entitiesToCreate)
        {
            entities.get(entityID).create(physicsWorld);
            createdEntities.add(entityID);
        }
        entitiesToCreate.removeAll(createdEntities);
        
        List<Entity> entityList = new ArrayList<Entity>(entities.values());
        for(Entity entity : entityList)
            if(entity.isCreated())
                entity.update(timeDelta);
        
        physicsWorld.update(timeDelta);
    }
}
