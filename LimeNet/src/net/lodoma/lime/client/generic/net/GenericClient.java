package net.lodoma.lime.client.generic.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import net.lodoma.lime.client.logic.CLBase;
import net.lodoma.lime.client.logic.CLChat;
import net.lodoma.lime.client.logic.CLConnectionCheck;
import net.lodoma.lime.client.logic.CLWorld;
import net.lodoma.lime.client.logic.ClientLogicPool;
import net.lodoma.lime.common.net.LogLevel;
import net.lodoma.lime.common.net.NetworkSettings;

public abstract class GenericClient
{
    private boolean isRunning = false;
    
    private int port;
    private InetAddress ipAddress;
    DatagramSocket socket;
   
    private ClientReader reader;
    
    private ClientLogicPool logicPool;
    private Map<String, Object> properties;
    
    public abstract void log(LogLevel level, String message);
    public abstract void log(LogLevel level, Exception exception);
    
    public final void open(int port, String ipAddress)
    {
        if(isRunning)
        {
            log(LogLevel.WARNING, new IllegalStateException("client is already open"));
            return;
        }
        
        try
        {
            this.port = port;
            this.ipAddress = InetAddress.getByName(ipAddress);
            socket = new DatagramSocket();
        }
        catch (UnknownHostException e)
        {
            log(LogLevel.SEVERE, e);
        }
        catch (SocketException e)
        {
            log(LogLevel.SEVERE, e);
        }
        
        logicPool = new ClientLogicPool(this);
        properties = new HashMap<String, Object>();
        
        logicPool.addLogic(new CLBase());
        logicPool.addLogic(new CLConnectionCheck());
        logicPool.addLogic(new CLChat());
        logicPool.addLogic(new CLWorld());
        
        logicPool.init();
        
        reader = new ClientReader(this);
        reader.start();
        
        isRunning = true;
        
        logicPool.start();
    }
    
    public final void close()
    {
        if(!isRunning)
        {
            log(LogLevel.WARNING, new IllegalStateException("client is already closed"));
            return;
        }

        logicPool.stop();
        
        reader.interrupt();
        socket.close();
        
        isRunning = false;
    }
    
    public void sendData(byte[] data)
    {
        if (!isRunning)
        {
            log(LogLevel.WARNING, new IllegalStateException("cannot send data while closed"));
            return;
        }
        if (data.length > NetworkSettings.MAX_PACKET_SIZE)
        {
            log(LogLevel.SEVERE, "packet too large to send [MAX_PACKET_SIZE=" + NetworkSettings.MAX_PACKET_SIZE + "]");
            return;
        }
        
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try
        {
            socket.send(packet);
        }
        catch (IOException e)
        {
            log(LogLevel.SEVERE, e);
        }
    }
    
    public boolean isRunning()
    {
        return isRunning;
    }
    
    public Object getProperty(String name)
    {
        return properties.get(name);
    }
    
    public void setProperty(String name, Object value)
    {
        properties.put(name, value);
    }
    
    public boolean hasProperty(String name)
    {
        return properties.containsKey(name);
    }
    
    public void removeProperty(String name)
    {
        properties.remove(name);
    }
}
