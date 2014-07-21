package net.lodoma.lime.client.logic;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientInputHandler;
import net.lodoma.lime.client.ClientOutput;
import net.lodoma.lime.client.ClientReader;
import net.lodoma.lime.client.io.base.CIHNetworkStageChange;
import net.lodoma.lime.client.io.base.CODependencyRequest;
import net.lodoma.lime.client.io.base.COPresenceResponse;
import net.lodoma.lime.common.NetStage;
import net.lodoma.lime.util.HashPool;
import net.lodoma.lime.util.Timer;

public class CLBase implements ClientLogic
{
    private Client client;
    private ClientReader reader;
    private HashPool<ClientInputHandler> cihPool;
    private HashPool<ClientOutput> coPool;
    
    private boolean sendFirstRequest;
    
    private Timer responseTimer;
    private double responseCounter;
    
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
        reader = (ClientReader) client.getProperty("reader");
    }

    @Override
    public void generalInit()
    {
        sendFirstRequest = true;
        coPool.add("Lime::DependencyRequest", new CODependencyRequest(client, "Lime::DependencyRequest"));
        coPool.add("Lime::PresenceResponse", new COPresenceResponse(client, "Lime::PresenceResponse"));
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
        
        if(responseTimer == null) responseTimer = new Timer();
        responseTimer.update();
        
        double timeDelta = responseTimer.getDelta();
        responseCounter += timeDelta;
        
        if(responseCounter >= 3.5)
        {
            coPool.get("Lime::PresenceResponse").handle();
            responseCounter -= 3.5;
        }
        
        try
        {
            reader.handleInput();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void clean()
    {
        
    }
}
