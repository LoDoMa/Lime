package net.lodoma.lime.client.generic.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import net.lodoma.lime.client.ClientData;
import net.lodoma.lime.client.generic.net.packet.ClientPacketPool;
import net.lodoma.lime.common.net.LogLevel;
import net.lodoma.lime.common.net.NetworkSettings;
import net.lodoma.lime.util.ThreadHelper;

public abstract class GenericClient
{
    private boolean isRunning = false;
    
    int port;
    InetAddress ipAddress;
    DatagramSocket socket;
   
    ClientReader reader;
    ClientLogic logic;
    
    private ClientData data;
    
    public abstract void log(LogLevel level, String message);
    public abstract void log(LogLevel level, Exception exception);
    
    public final void open(int port, String ipAddress, ClientLogic logic, ClientData data)
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
        
        this.data = data;
        this.data.packetPool = new ClientPacketPool();
        
        reader = new ClientReader(this);
        reader.start();
        isRunning = true;
        
        this.logic = logic;
        logic.setClient(this);
        logic.onOpen();
        logic.start();
    }
    
    public final void close()
    {
        if(!isRunning)
        {
            log(LogLevel.WARNING, new IllegalStateException("client is already closed"));
            return;
        }

        try
        {
            ThreadHelper.interruptAndWait(logic);
        }
        catch (InterruptedException e)
        {
            log(LogLevel.SEVERE, e);
        }
        logic.onClose();
        
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
    
    public ClientData getData()
    {
        return data;
    }
}
