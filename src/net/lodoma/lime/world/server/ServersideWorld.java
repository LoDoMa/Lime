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
import net.lodoma.lime.server.logic.UserManager;
import net.lodoma.lime.util.HashPool;
import net.lodoma.lime.world.entity.EntityWorld;
import net.lodoma.lime.world.platform.Platform;

public class ServersideWorld implements EntityWorld
{
    private final class SendOnEvent implements EventListener
    {
        private ServersideWorld world;
        private EventManager manager;
        private ServerOutput platformOutput;
        private ServerOutput entityOutput;
        
        public SendOnEvent(ServersideWorld world, EventManager manager,
                ServerOutput entityOutput, ServerOutput platformOutput)
        {
            this.world = world;
            this.manager = manager;
            this.platformOutput = platformOutput;
            this.entityOutput = entityOutput;
            
            manager.addListener(this);
        }
        
        @Override
        public void onEvent(Object eventObject)
        {
            ServerUser user = (ServerUser) eventObject;
            
            List<Platform> platformList = world.getPlatformList();
            for(Platform platform : platformList)
                platformOutput.handle(user, platform);
            
            Set<Integer> entityIDSet = world.getEntityIDSet();
            for(Integer id : entityIDSet)
                entityOutput.handle(user, world.getEntity(id));
        }
        
        public void remove()
        {
            manager.removeListener(this);
        }
    }
    
    private Server server;
    private UserManager userManager;
    
    private PhysicsWorld physicsWorld;
    private List<Platform> platforms;
    private Map<Integer, Entity> entities;
    
    private SendOnEvent initialWorldDataSender;
    private ServerOutput platformCreation;
    private ServerOutput entityCreation;
    
    public ServersideWorld(Server server)
    {
        this.server = server;
        
        physicsWorld = new PhysicsWorld();
        platforms = new ArrayList<Platform>();
        entities = new HashMap<Integer, Entity>();
    }
    
    @Override
    public boolean isServer()
    {
        return true;
    }
    
    public Server getServer()
    {
        return server;
    }
    
    @SuppressWarnings("unchecked")
    public void generalInit()
    {
        userManager = (UserManager) server.getProperty("userManager");
        
        HashPool<ServerOutput> soPool = (HashPool<ServerOutput>) server.getProperty("soPool");
        EventManager manager = ((HashPool<EventManager>) server.getProperty("emanPool")).get("Lime::onNewUser");

        platformCreation = soPool.get("Lime::PlatformCreation");
        entityCreation = soPool.get("Lime::EntityCreation");
        
        initialWorldDataSender = new SendOnEvent(this, manager, entityCreation, platformCreation);
    }
    
    public void clean()
    {
        initialWorldDataSender.remove();
        
        for(Platform platform : platforms)
            platform.destroy(physicsWorld);
        platforms.clear();
        
        List<Entity> entityList = new ArrayList<Entity>(entities.values());
        for(Entity entity : entityList)
            entity.destroy(physicsWorld);
        entities.clear();
    }
    
    public PhysicsWorld getPhysicsWorld()
    {
        return physicsWorld;
    }
    
    public void addPlatform(Platform platform)
    {
        platform.create(physicsWorld);
        platforms.add(platform);
        
        List<ServerUser> userList = userManager.getUserList();
        for(ServerUser user : userList)
            platformCreation.handle(user, platform);
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
        
        List<ServerUser> userList = userManager.getUserList();
        for(ServerUser user : userList)
            entityCreation.handle(user, entity);
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
}
