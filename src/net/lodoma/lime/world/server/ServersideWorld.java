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
import net.lodoma.lime.server.ServerOutput;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.server.event.EventListener;
import net.lodoma.lime.server.event.EventManager;
import net.lodoma.lime.util.HashPool;
import net.lodoma.lime.world.entity.EntityWorld;

public class ServersideWorld implements EntityWorld
{
    private final class SendOnEvent implements EventListener
    {
        private EventManager manager;
        private ServerOutput output;
        
        public SendOnEvent(EventManager manager, ServerOutput output)
        {
            this.manager = manager;
            this.output = output;
            
            manager.addListener(this);
        }
        
        @Override
        public void onEvent(Object eventObject)
        {
            ServerUser user = (ServerUser) eventObject;
            output.handle(user);
        }
        
        public void remove()
        {
            manager.removeListener(this);
        }
    }
    
    private Server server;
    
    private PhysicsWorld physicsWorld;
    private Map<Integer, Entity> entities;
    private List<Integer> entitiesToCreate;
    
    private SendOnEvent initialWorldDataSender;
    
    public ServersideWorld(Server server)
    {
        this.server = server;
        
        physicsWorld = new PhysicsWorld();
        entities = new HashMap<Integer, Entity>();
        entitiesToCreate = new ArrayList<Integer>();
    }
    
    @Override
    public boolean isServer()
    {
        return true;
    }
    
    @SuppressWarnings("unchecked")
    public void generalInit()
    {
        EventManager manager = ((HashPool<EventManager>) server.getProperty("emanPool")).get("Lime::onNewUser");
        ServerOutput output = ((HashPool<ServerOutput>) server.getProperty("soPool")).get("Lime::InitialWorldData");
        initialWorldDataSender = new SendOnEvent(manager, output);
    }
    
    public void clean()
    {
        initialWorldDataSender.remove();
        
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
