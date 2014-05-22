package net.lodoma.lime.net.client;

import net.lodoma.lime.net.client.generic.ClientLogic;
import net.lodoma.lime.net.packet.ConnectionCheckPacket;

public class LimeClientLogic extends ClientLogic
{
    private boolean checkedConnection;
    
    @Override
    public void logic()
    {
        if(client.hasProperty("lastConnectionTime"))
        {
            long currentTime = System.currentTimeMillis();
            long lastTime = (Long) client.getProperty("lastConnectionTime");
            long timeDelta = currentTime - lastTime;
            if(timeDelta >= 1000)
            {
                if(!checkedConnection)
                {
                    client.getCommonHandler().getPacketHandler(ConnectionCheckPacket.class).sendHeader(client);
                    checkedConnection = true;
                }
                if(timeDelta >= 2000)
                {
                    System.out.println("server not responding");
                }
            }
            else
                checkedConnection = false;
        }
    }
}
