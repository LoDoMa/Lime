package net.lodoma.lime.client.logic;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientInputHandler;
import net.lodoma.lime.client.ClientOutput;
import net.lodoma.lime.client.io.base.CIHNetworkStageChange;
import net.lodoma.lime.client.io.base.CODependencyRequest;
import net.lodoma.lime.common.net.NetStage;
import net.lodoma.lime.util.HashPool;

public class CLBase implements ClientLogic
{
    private Client client;
    private HashPool<ClientInputHandler> cihPool;
    private HashPool<ClientOutput> coPool;
    
    private boolean sendFirstRequest;
    
    @Override
    public void baseInit(Client client)
    {
        this.client = client;
    }

    @Override
    public void propertyInit()
    {
        client.setProperty("networkStage", NetStage.DEPENDENCY);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void fetchInit()
    {
        cihPool = (HashPool<ClientInputHandler>) client.getProperty("cihPool");
        coPool = (HashPool<ClientOutput>) client.getProperty("coPool");
    }

    @Override
    public void generalInit()
    {
        sendFirstRequest = true;
        coPool.add("Lime::DependencyRequest", new CODependencyRequest(client, "Lime::DependencyRequest"));
        cihPool.add("Lime::NetworkStageChange", new CIHNetworkStageChange(client));
    }
    
    @Override
    public void logic()
    {
        if(sendFirstRequest)
        {
            client.setProperty("dependencyProgress", 0);
            coPool.get("Lime::DependencyRequest").handle();
            sendFirstRequest = false;
        }
    }
    
    @Override
    public void clean()
    {
        
    }
}
