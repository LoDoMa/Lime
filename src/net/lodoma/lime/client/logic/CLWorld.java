package net.lodoma.lime.client.logic;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientInputHandler;
import net.lodoma.lime.client.ClientOutput;
import net.lodoma.lime.client.io.world.CIHInitialWorldData;
import net.lodoma.lime.client.io.world.COInitialWorldRequest;
import net.lodoma.lime.util.HashPool;
import net.lodoma.lime.world.client.ClientsideWorld;

public class CLWorld implements ClientLogic
{
    private Client client;
    private HashPool<ClientInputHandler> cihPool;
    private HashPool<ClientOutput> coPool;
    private ClientsideWorld world;
    
    @Override
    public void baseInit(Client client)
    {
        this.client = client;
    }
    
    @Override
    public void propertyInit()
    {
        client.setProperty("world", new ClientsideWorld(client));
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void fetchInit()
    {
        cihPool = (HashPool<ClientInputHandler>) client.getProperty("cihPool");
        coPool = (HashPool<ClientOutput>) client.getProperty("coPool");
        world = (ClientsideWorld) client.getProperty("world");
        world.fetch();
    }
    
    @Override
    public void generalInit()
    {
        coPool.add("Lime::InitialWorldRequest", new COInitialWorldRequest(client, "Lime::InitialWorldRequest"));
        cihPool.add("Lime::InitialWorldData", new CIHInitialWorldData(client));
    }
    
    @Override
    public void clean()
    {
        
    }
    
    @Override
    public void logic()
    {
        world.update(0);
    }
}
