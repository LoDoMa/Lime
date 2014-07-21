package net.lodoma.lime.server.logic;

import java.util.List;
import java.util.Set;

import net.lodoma.lime.physics.entity.Entity;
import net.lodoma.lime.physics.entity.EntityLoader;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerInputHandler;
import net.lodoma.lime.server.ServerOutput;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.server.io.world.SOEntityCorrection;
import net.lodoma.lime.server.io.world.SOInitialWorldData;
import net.lodoma.lime.util.HashPool;
import net.lodoma.lime.util.Timer;
import net.lodoma.lime.world.builder.WorldFileLoader;
import net.lodoma.lime.world.server.ServersideWorld;

public class SLWorld implements ServerLogic
{
    private Server server;
    @SuppressWarnings("unused")
    private HashPool<ServerInputHandler> sihPool;
    private HashPool<ServerOutput> soPool;
    
    private UserManager userManager;
    private ServersideWorld world;
    
    private Timer timer;
    
    private double correctionTime = 0;
    private double correctionGoal = 2;
    
    @Override
    public void baseInit(Server server)
    {
        this.server = server;
    }
    
    @Override
    public void propertyInit()
    {
        server.setProperty("world", new ServersideWorld(server));
        server.setProperty("entityLoader", new EntityLoader());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void fetchInit()
    {
        sihPool = (HashPool<ServerInputHandler>) server.getProperty("sihPool");
        soPool = (HashPool<ServerOutput>) server.getProperty("soPool");
        userManager = (UserManager) server.getProperty("userManager");
        world = (ServersideWorld) server.getProperty("world");
    }
    
    @Override
    public void generalInit()
    {
        soPool.add("Lime::InitialWorldData", new SOInitialWorldData(server, "Lime::InitialWorldData"));
        soPool.add("Lime::EntityCorrection", new SOEntityCorrection(server, "Lime::EntityCorrection"));
        
        world.generalInit();
        
        WorldFileLoader fileLoader = new WorldFileLoader();
        fileLoader.build(world);
    }
    
    @Override
    public void clean()
    {
        world.clean();
    }
    
    @Override
    public void logic()
    {
        if(timer == null) timer = new Timer();
        timer.update();
        
        double timeDelta = timer.getDelta();
        correctionTime += timeDelta;
        if(correctionTime >= correctionGoal)
        {
            correctionTime -= correctionGoal;
            
            ServerOutput serverOutput = soPool.get("Lime::EntityCorrection");
            
            List<ServerUser> serverUsers = userManager.getUserList();
            Set<Integer> entityIDSet = world.getEntityIDSet();
            
            for(ServerUser serverUser : serverUsers)
                for(int entityID : entityIDSet)
                {
                    Entity entity = world.getEntity(entityID);
                    if(entity.isCreated())
                        serverOutput.handle(serverUser, world.getEntity(entityID));
                }
        }
        
        world.update(timeDelta);
    }
}
