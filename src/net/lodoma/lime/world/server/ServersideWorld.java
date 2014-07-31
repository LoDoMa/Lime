package net.lodoma.lime.world.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.lodoma.lime.common.NetworkSide;
import net.lodoma.lime.event.EventBundle;
import net.lodoma.lime.event.EventListener;
import net.lodoma.lime.event.EventManager;
import net.lodoma.lime.event.InvalidEventBundleException;
import net.lodoma.lime.physics.PhysicsWorld;
import net.lodoma.lime.physics.entity.Entity;
import net.lodoma.lime.physics.entity.EntityWorld;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerPacket;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.server.io.entity.SPEntityCorrection;
import net.lodoma.lime.server.io.entity.SPEntityCreation;
import net.lodoma.lime.server.io.world.SPPlatformCreation;
import net.lodoma.lime.server.logic.UserManager;
import net.lodoma.lime.util.HashPool32;
import net.lodoma.lime.world.platform.Platform;

public class ServersideWorld implements EntityWorld
{
    private final class SendOnEvent implements EventListener
    {
        private ServersideWorld world;
        private EventManager manager;
        private ServerPacket platformCreation;
        private ServerPacket entityCreation;
        private ServerPacket entityCorrection;
        
        public SendOnEvent(ServersideWorld world, EventManager manager)
        {
            this.world = world;
            this.manager = manager;
            this.platformCreation = world.platformCreation;
            this.entityCreation = world.entityCreation;
            this.entityCorrection = world.entityCorrection;
            
            manager.addListener(this);
        }
        
        @Override
        public void onEvent(EventBundle bundle)
        {
            if(!bundle.has("userID"))
                throw new InvalidEventBundleException();
            
            Object userID = bundle.get("userID");
            if(!(userID instanceof Integer))
                throw new InvalidEventBundleException();
            ServerUser user = userManager.getUser((Integer) userID);
            
            List<Platform> platformList = world.getPlatformList();
            for(Platform platform : platformList)
                platformCreation.write(user, platform);

            List<Entity> entityList = new ArrayList<Entity>(entities.values());
            for(Entity entity : entityList)
            {
                entityCreation.write(user, entity);
                entityCorrection.write(user, entity);
            }
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
    private ServerPacket platformCreation;
    private ServerPacket entityCreation;
    private ServerPacket entityCorrection;
    
    private static final double CORRECTION_TIME = 1.5;
    private double correctionRemaining;
    
    public ServersideWorld(Server server)
    {
        this.server = server;
        
        physicsWorld = new PhysicsWorld();
        platforms = new ArrayList<Platform>();
        entities = new HashMap<Integer, Entity>();
    }
    
    @Override
    public NetworkSide getNetworkSide()
    {
        return NetworkSide.SERVER;
    }
    
    public Server getServer()
    {
        return server;
    }
    
    @SuppressWarnings("unchecked")
    public void generalInit()
    {
        userManager = (UserManager) server.getProperty("userManager");
        
        HashPool32<ServerPacket> spPool = (HashPool32<ServerPacket>) server.getProperty("spPool");
        EventManager manager = ((HashPool32<EventManager>) server.getProperty("emanPool")).get(EventManager.ON_NEW_USER_HASH);

        platformCreation = spPool.get(SPPlatformCreation.HASH);
        entityCreation = spPool.get(SPEntityCreation.HASH);
        entityCorrection = spPool.get(SPEntityCorrection.HASH);
        
        initialWorldDataSender = new SendOnEvent(this, manager);
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

        Set<ServerUser> userSet = userManager.getUserSet();
        for(ServerUser user : userSet)
            platformCreation.write(user, platform);
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

        Set<ServerUser> userSet = userManager.getUserSet();
        for(ServerUser user : userSet)
            entityCreation.write(user, entity);
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
        correctionRemaining += timeDelta;
        while(correctionRemaining >= CORRECTION_TIME)
        {
            correctionRemaining -= CORRECTION_TIME;
            
            List<Entity> entityList = new ArrayList<Entity>(entities.values());
            Set<ServerUser> userSet = userManager.getUserSet();
            for(ServerUser user : userSet)
                for(Entity entity : entityList)
                    if(entity.isCreated())
                        entityCorrection.write(user, entity);
        }
        
        List<Entity> entityList = new ArrayList<Entity>(entities.values());
        for(Entity entity : entityList)
            if(entity.isCreated())
                entity.update(timeDelta);
        
        physicsWorld.update(timeDelta);
    }
}
