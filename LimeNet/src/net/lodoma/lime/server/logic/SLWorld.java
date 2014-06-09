package net.lodoma.lime.server.logic;

import net.lodoma.lime.server.generic.GenericServer;
import net.lodoma.lime.world.ServersideWorld;

public class SLWorld implements ServerLogic
{
    private GenericServer server;
    private ServersideWorld world;
    
    @Override
    public void baseInit(GenericServer server)
    {
        this.server = server;
    }
    
    @Override
    public void propertyInit()
    {
        server.setProperty("world", new ServersideWorld(server));
    }
    
    @Override
    public void fetchInit()
    {
        world = (ServersideWorld) server.getProperty("world");
        world.fetch();
    }
    
    @Override
    public void generalInit()
    {
        
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
