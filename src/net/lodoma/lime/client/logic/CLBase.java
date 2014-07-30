package net.lodoma.lime.client.logic;

import java.io.DataInputStream;
import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientInputHandler;
import net.lodoma.lime.client.ClientOutput;
import net.lodoma.lime.client.io.base.CIHModificationCheck;
import net.lodoma.lime.client.io.base.CIHNetworkStageChange;
import net.lodoma.lime.client.io.base.CODependencyRequest;
import net.lodoma.lime.client.io.base.COPresenceResponse;
import net.lodoma.lime.common.NetStage;
import net.lodoma.lime.util.HashPool32;
import net.lodoma.lime.util.Timer;

public class CLBase implements ClientLogic
{
    private Client client;
    private HashPool32<ClientInputHandler> cihPool;
    private HashPool32<ClientOutput> coPool;
    
    private boolean sendFirstRequest;
    
    private DataInputStream stream;
    
    private Timer responseTimer;
    private double responseCounter;
    
    @Override
    public void baseInit(Client client)
    {
        this.client = client;
        stream = client.getInputStream();
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
        cihPool = (HashPool32<ClientInputHandler>) client.getProperty("cihPool");
        coPool = (HashPool32<ClientOutput>) client.getProperty("coPool");
    }

    @Override
    public void generalInit()
    {
        sendFirstRequest = true;
        coPool.add(CODependencyRequest.HASH, new CODependencyRequest(client));
        coPool.add(COPresenceResponse.HASH, new COPresenceResponse(client));
        cihPool.add(CIHNetworkStageChange.HASH, new CIHNetworkStageChange(client));
        
        cihPool.add(CIHModificationCheck.HASH, new CIHModificationCheck(client));
    }
    
    @Override
    public void logic()
    {
        if(sendFirstRequest)
        {
            client.setProperty("dependencyProgress", 0);
            coPool.get(CODependencyRequest.HASH).handle();
            sendFirstRequest = false;
        }
        
        if(responseTimer == null) responseTimer = new Timer();
        responseTimer.update();
        
        double timeDelta = responseTimer.getDelta();
        responseCounter += timeDelta;
        
        if(responseCounter >= 3.5)
        {
            coPool.get(COPresenceResponse.HASH).handle();
            responseCounter -= 3.5;
        }
        
        try
        {
            while(stream.available() >= 4)
            {
                int hash = stream.readInt();
                ClientInputHandler cih = cihPool.get(hash);
                if(cih != null)
                    cih.handle();
            }
        }
        catch(IOException e)
        {
            client.setCloseMessage("Client logic exception");
            client.close();
        }
    }
    
    @Override
    public void clean()
    {
        
    }
}
