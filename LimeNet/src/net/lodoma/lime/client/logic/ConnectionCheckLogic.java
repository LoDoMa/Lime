package net.lodoma.lime.client.logic;

import net.lodoma.lime.client.generic.net.ClientLogic;
import net.lodoma.lime.client.generic.net.GenericClient;
import net.lodoma.lime.client.generic.net.packet.ClientPacketPool;

public class ConnectionCheckLogic implements ClientLogic
{
    private GenericClient client;
    private ClientPacketPool packetPool;
    
    private boolean checkedConnection;
    
    @Override
    public void baseInit(GenericClient client)
    {
        this.client = client;
    }

    @Override
    public void propertyInit()
    {
        client.setProperty("lastServerResponse", System.currentTimeMillis());
    }

    @Override
    public void fetchInit()
    {
        packetPool = (ClientPacketPool) client.getProperty("packetPool");
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
        long timeDelta = System.currentTimeMillis() - (Long) client.getProperty("lastServerResponse");
        if (timeDelta >= 1000)
        {
            if (!checkedConnection)
            {
                packetPool.getPacket("Lime::ResponseRequest").send(client);
                checkedConnection = true;
            }
            if (timeDelta >= 2000)
            {
                System.out.println("server not responding");
            }
        }
        else
            checkedConnection = false;
    }
}
