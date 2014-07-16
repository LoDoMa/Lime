package net.lodoma.lime.server.logic;

import net.lodoma.lime.physics.PhysicsBody;
import net.lodoma.lime.physics.PhysicsBodyType;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerInputHandler;
import net.lodoma.lime.server.ServerOutput;
import net.lodoma.lime.server.io.world.SIHInitialWorldRequest;
import net.lodoma.lime.server.io.world.SOInitialWorldData;
import net.lodoma.lime.util.HashPool;
import net.lodoma.lime.util.Vector2;
import net.lodoma.lime.world.builder.WorldFileLoader;
import net.lodoma.lime.world.server.ServersideWorld;

public class SLWorld implements ServerLogic
{
    private Server server;
    private HashPool<ServerInputHandler> sihPool;
    private HashPool<ServerOutput> soPool;
    private ServersideWorld world;
    
    @Override
    public void baseInit(Server server)
    {
        this.server = server;
    }
    
    @Override
    public void propertyInit()
    {
        server.setProperty("world", new ServersideWorld(server));
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void fetchInit()
    {
        sihPool = (HashPool<ServerInputHandler>) server.getProperty("sihPool");
        soPool = (HashPool<ServerOutput>) server.getProperty("soPool");
        world = (ServersideWorld) server.getProperty("world");
        world.fetch();
    }
    
    @Override
    public void generalInit()
    {
        sihPool.add("Lime::InitialWorldRequest", new SIHInitialWorldRequest(server));
        soPool.add("Lime::InitialWorldData", new SOInitialWorldData(server, "Lime::InitialWorldData"));
        
        WorldFileLoader fileLoader = new WorldFileLoader();
        fileLoader.build(world);
        
        PhysicsBody body = new PhysicsBody();
        body.generateID();
        body.setBodyType(PhysicsBodyType.STATIC);
        body.setPosition(new Vector2(1, 1));
        body.setPolygonShape(
                new Vector2(0.0f, 0.0f),
                new Vector2(1.0f, 0.0f),
                new Vector2(1.0f, 1.0f),
                new Vector2(0.0f, 1.0f));
        body.reload();
        body.create(world.getPhysicsWorld());
        world.getPhysicsWorld().getPool().addBody(body);
    }
    
    @Override
    public void clean()
    {
        
    }
    
    @Override
    public void logic()
    {
        
    }
}
