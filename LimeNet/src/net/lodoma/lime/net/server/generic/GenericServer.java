package net.lodoma.lime.net.server.generic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.lodoma.lime.net.LogLevel;
import net.lodoma.lime.net.NetworkSettings;
import net.lodoma.lime.net.packet.generic.ServerPacketPool;

public abstract class GenericServer
{
    private boolean isRunning = false;
    
    DatagramSocket socket;
    
    UserPool userPool;
    ServerReader reader;
    ServerLogic logic;
    
    Map<String, Object> properties;
    
    public abstract void onOpen();
    
    public abstract void onClose();
    
    public abstract void log(LogLevel level, String message);
    
    public abstract void log(LogLevel level, Exception exception);
    
    public final void open(int port, ServerLogic logic)
    {
        if (isRunning)
        {
            log(LogLevel.WARNING, new IllegalStateException("server is already open"));
            return;
        }
        
        try
        {
            socket = new DatagramSocket(port);
        }
        catch (SocketException e)
        {
            log(LogLevel.SEVERE, e);
        }
        
        properties = new HashMap<String, Object>();
        userPool = new UserPool();
        
        setProperty("packetPool", new ServerPacketPool(this));
        setProperty("userPool", userPool);
        
        reader = new ServerReader(this);
        reader.start();
        this.logic = logic;
        logic.setServer(this);
        logic.start();
        
        isRunning = true;
        
        onOpen();
        logic.onOpen();
    }
    
    public final void close()
    {
        if (!isRunning)
        {
            log(LogLevel.WARNING, new IllegalStateException("server is already closed"));
            return;
        }
        
        logic.interrupt();
        reader.interrupt();
        socket.close();
        
        isRunning = false;
        
        onClose();
        logic.onClose();
    }
    
    public final void sendData(byte[] data, ServerUser user)
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
        
        DatagramPacket packet = new DatagramPacket(data, data.length, user.address, user.port);
        try
        {
            socket.send(packet);
        }
        catch (IOException e)
        {
            log(LogLevel.SEVERE, e);
        }
    }
    
    public final void sendDataToAll(byte[] data)
    {
        Set<ServerUser> allUsers = ((UserPool) getProperty("userPool")).getUserSet();
        for(ServerUser user : allUsers)
            sendData(data, user);
    }
    
    public final Object getProperty(String name)
    {
        return properties.get(name);
    }
    
    public final void setProperty(String name, Object value)
    {
        properties.put(name, value);
    }
    
    public final void removeProperty(String name)
    {
        properties.remove(name);
    }
    
    public final boolean hasProperty(String name)
    {
        return properties.containsKey(name);
    }
    
    public final void clearProperties()
    {
        properties.clear();
    }
}
