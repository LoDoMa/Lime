package net.lodoma.lime.client.logic;

import java.io.DataInputStream;
import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacketHandler;
import net.lodoma.lime.client.ClientPacket;
import net.lodoma.lime.client.io.base.CPHModificationCheck;
import net.lodoma.lime.client.io.base.CPHNetworkStageChange;
import net.lodoma.lime.client.io.base.CPDependencyRequest;
import net.lodoma.lime.client.io.base.CPPresenceResponse;
import net.lodoma.lime.common.NetStage;
import net.lodoma.lime.util.HashPool32;
import net.lodoma.lime.util.Timer;

public class CLBase implements ClientLogic
{
    private Client client;
    private HashPool32<ClientPacketHandler> cphPool;
    private HashPool32<ClientPacket> cpPool;
    
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
        cphPool = (HashPool32<ClientPacketHandler>) client.getProperty("cphPool");
        cpPool = (HashPool32<ClientPacket>) client.getProperty("cpPool");
    }

    @Override
    public void generalInit()
    {
        sendFirstRequest = true;
        cpPool.add(CPDependencyRequest.HASH, new CPDependencyRequest(client));
        cpPool.add(CPPresenceResponse.HASH, new CPPresenceResponse(client));
        cphPool.add(CPHNetworkStageChange.HASH, new CPHNetworkStageChange(client));
        
        cphPool.add(CPHModificationCheck.HASH, new CPHModificationCheck(client));
    }
    
    @Override
    public void logic()
    {
        if(sendFirstRequest)
        {
            client.setProperty("dependencyProgress", 0);
            cpPool.get(CPDependencyRequest.HASH).write();
            sendFirstRequest = false;
        }
        
        if(responseTimer == null) responseTimer = new Timer();
        responseTimer.update();
        
        double timeDelta = responseTimer.getDelta();
        responseCounter += timeDelta;
        
        if(responseCounter >= 3.5)
        {
            cpPool.get(CPPresenceResponse.HASH).write();
            responseCounter -= 3.5;
        }
        
        try
        {
            int amountHandled = 0;
            while(stream.available() >= 4 && amountHandled < 100)
            {
                int hash = stream.readInt();
                ClientPacketHandler cih = cphPool.get(hash);
                if(cih != null)
                    cih.handle();
                amountHandled++;
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
