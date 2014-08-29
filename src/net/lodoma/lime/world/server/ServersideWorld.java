package net.lodoma.lime.world.server;

import java.util.List;
import java.util.Set;

import net.lodoma.lime.common.NetworkSide;
import net.lodoma.lime.common.PropertyPool;
import net.lodoma.lime.event.EventBundle;
import net.lodoma.lime.event.EventListener;
import net.lodoma.lime.event.EventManager;
import net.lodoma.lime.event.InvalidEventBundleException;
import net.lodoma.lime.physics.entity.Entity;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerPacket;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.server.io.entity.SPEntityCorrection;
import net.lodoma.lime.server.io.entity.SPEntityCreation;
import net.lodoma.lime.server.io.entity.SPSetActor;
import net.lodoma.lime.server.logic.UserManager;
import net.lodoma.lime.util.HashPool32;
import net.lodoma.lime.world.CommonWorld;

public class ServersideWorld extends CommonWorld
{
    private final class SendOnEvent implements EventListener
    {
        private ServersideWorld world;
        private EventManager manager;
        private ServerPacket entityCreation;
        private ServerPacket entityCorrection;
        
        public SendOnEvent(ServersideWorld world, EventManager manager)
        {
            this.world = world;
            this.manager = manager;
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

            List<Entity> entityList = world.getEntityList();
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
    
    private SendOnEvent initialWorldDataSender;
    private ServerPacket entityCreation;
    private ServerPacket entityCorrection;
    private ServerPacket setActor;
    
    private static final double CORRECTION_TIME = 5.0;
    private double correctionRemaining;
    private int currentEntity;
    
    public ServersideWorld(Server server)
    {
        this.server = server;
    }
    
    @Override
    public NetworkSide getNetworkSide()
    {
        return NetworkSide.SERVER;
    }
    
    @Override
    public PropertyPool getPropertyPool()
    {
        return server;
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
        
        entityCreation = spPool.get(SPEntityCreation.HASH);
        entityCorrection = spPool.get(SPEntityCorrection.HASH);
        setActor = spPool.get(SPSetActor.HASH);
        
        initialWorldDataSender = new SendOnEvent(this, manager);
    }
    
    @Override
    public void clean()
    {
        super.clean();
        initialWorldDataSender.remove();
    }
    
    @Override
    public int newEntity(int hash)
    {
        int id = super.newEntity(hash);
        Set<ServerUser> userSet = userManager.getUserSet();
        for(ServerUser user : userSet)
            entityCreation.write(user, getEntity(id));
        return id;
    }
    
    public void setActor(int entityID, int userID)
    {
        ServerUser user = userManager.getUser(userID);
        setActor.write(user, entityID);
    }
    
    public void update(double timeDelta)
    {
        if(entityPool.size() > 0)
        {
            correctionRemaining += timeDelta;
            double time = CORRECTION_TIME / (double) entityPool.size();
            if(time < 0.1) time = 0.1;
            if(correctionRemaining >= time)
            {
                correctionRemaining = 0;
    
                List<Entity> entityList = getEntityList();
                if(currentEntity < 0 || currentEntity >= entityList.size())
                    currentEntity = 0;
                Entity entity = entityList.get(currentEntity++);
                
                Set<ServerUser> userSet = userManager.getUserSet();
                for(ServerUser user : userSet)
                    entityCorrection.write(user, entity);
            }
        }
        
        super.update(timeDelta);
    }
}
