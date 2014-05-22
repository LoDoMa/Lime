package net.lodoma.lime.net.client;

import java.lang.reflect.InvocationTargetException;

import net.lodoma.lime.mod.Module;
import net.lodoma.lime.net.packet.LimeCommonHandler;
import net.lodoma.lime.net.packet.LoginPacket;
import net.lodoma.lime.net.packet.VisualInstanceReadyPacket;
import net.lodoma.lime.world.World;

public class LimeVisualInstance
{
    public static enum DisconnectReason
    {
        CONNECTION_TIMEOUT,
        CANNOT_REACH_SERVER,
        REJECTED_BY_SERVER,
        
    }
    
    private World world;
    private LimeClient client;
    
    public LimeVisualInstance()
    {
        world = new World();
        client = new LimeClient();
    }
    
    private boolean requestJoin()
    {
        if(client.hasProperty("accepted"))
            client.removeProperty("accepted");
        client.getCommonHandler().getPacketHandler(LoginPacket.class).sendHeader(client);
        long requestStart = System.currentTimeMillis();
        boolean timeout = false;
        while(!client.hasProperty("accepted"))
            try
            {
                long requestCurrent = System.currentTimeMillis();
                if((requestCurrent - requestStart) >= 1000)
                {
                    timeout = true;
                    break;
                }
                Thread.sleep(1);
            }
            catch (InterruptedException e)
            {
                return false;
            }
        if(timeout) return false;
        boolean accepted = (Boolean) client.getProperty("accepted");
        return accepted;
    }
    
    private void setProperties()
    {
        client.setProperty("visualInstance", this);
        client.setProperty("world", world);
    }
    
    private boolean requestDependencies()
    {
        return true;
    }
    
    private boolean requestUserStatus()
    {
        client.getCommonHandler().getPacketHandler(VisualInstanceReadyPacket.class);
        return true;
    }
    
    private boolean connect()
    {
        client.open(19523, "localhost", new LimeCommonHandler(), new LimeClientLogic());
        if(!requestJoin()) return false;
        setProperties();
        if(!requestDependencies()) return false;
        if(!requestUserStatus()) return false;
        return true;
    }
    
    public void run()
    {
        if(!connect())
        {
            System.err.println("failed to connect");
            client.close();
            return;
        }
        System.out.println("connected");
    }
}
