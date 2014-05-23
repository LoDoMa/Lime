package net.lodoma.lime.net.client.generic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public abstract class GenericClient
{
    private boolean isRunning = false;
    
    int port;
    InetAddress ipAddress;
    DatagramSocket socket;
   
    ClientReader reader;
    ClientLogic logic;
    
    Map<String, Object> properties;
    
    public abstract void handleException(Exception exception);
    
    public final void open(int port, String ipAddress, ClientLogic logic)
    {
        if(isRunning)
        {
            handleException(new IllegalStateException("client is already open"));
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
            handleException(e);
        }
        catch (SocketException e)
        {
            handleException(e);
        }
        
        properties = new HashMap<String, Object>();
        setProperty("packetPool", new ClientPacketPool());
        
        reader = new ClientReader(this);
        reader.start();
        this.logic = logic;
        logic.setClient(this);
        logic.start();
        
        isRunning = true;
    }
    
    public final void close()
    {
        if(!isRunning)
        {
            handleException(new IllegalStateException("client is already closed"));
            return;
        }
        
        logic.interrupt();
        reader.interrupt();
        socket.close();
        
        isRunning = false;
    }
    
    public void sendData(byte[] data)
    {
        if (!isRunning)
        {
            handleException(new IllegalStateException("cannot send data while closed"));
            return;
        }
        
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try
        {
            socket.send(packet);
        }
        catch (IOException e)
        {
            handleException(e);
        }
    }
    
    public Object getProperty(String name)
    {
        return properties.get(name);
    }
    
    public void setProperty(String name, Object value)
    {
        properties.put(name, value);
    }
    
    public void removeProperty(String name)
    {
        properties.remove(name);
    }
    
    public boolean hasProperty(String name)
    {
        return properties.containsKey(name);
    }
    
    public void clearProperties()
    {
        properties.clear();
    }
}
