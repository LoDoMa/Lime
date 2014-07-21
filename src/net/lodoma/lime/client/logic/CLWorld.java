package net.lodoma.lime.client.logic;

import java.io.File;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientInputHandler;
import net.lodoma.lime.client.ClientOutput;
import net.lodoma.lime.client.io.world.CIHEntityCorrection;
import net.lodoma.lime.client.io.world.CIHEntityCreation;
import net.lodoma.lime.physics.entity.EntityLoader;
import net.lodoma.lime.util.HashPool;
import net.lodoma.lime.util.Timer;
import net.lodoma.lime.world.client.ClientsideWorld;

public class CLWorld implements ClientLogic
{
    private Client client;
    private HashPool<ClientInputHandler> cihPool;
    @SuppressWarnings("unused")
    private HashPool<ClientOutput> coPool;
    private ClientsideWorld world;
    private EntityLoader entityLoader;
    
    private Timer timer;
    
    @Override
    public void baseInit(Client client)
    {
        this.client = client;
    }
    
    @Override
    public void propertyInit()
    {
        client.setProperty("world", new ClientsideWorld(client));
        client.setProperty("entityLoader", new EntityLoader());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void fetchInit()
    {
        cihPool = (HashPool<ClientInputHandler>) client.getProperty("cihPool");
        coPool = (HashPool<ClientOutput>) client.getProperty("coPool");
        world = (ClientsideWorld) client.getProperty("world");
        entityLoader = (EntityLoader) client.getProperty("entityLoader");
        
        world.fetch();
    }
    
    @Override
    public void generalInit()
    {
        cihPool.add("Lime::EntityCreation", new CIHEntityCreation(client));
        cihPool.add("Lime::EntityCorrection", new CIHEntityCorrection(client));
        
        entityLoader.addXMLFile("lime::zombie", new File("model/zombie.xml"));
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
        world.update(timer.getDelta());
    }
}
