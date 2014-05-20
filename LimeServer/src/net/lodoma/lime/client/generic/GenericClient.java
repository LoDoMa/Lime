package net.lodoma.lime.client.generic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import net.lodoma.lime.packet.generic.CommonHandler;

public abstract class GenericClient
{
    private boolean isRunning = false;
    
    int port;
    InetAddress ipAddress;
    DatagramSocket socket;
    
    CommonHandler handler;
    ClientReader reader;
    
    public abstract void handleException(Exception exception);
    
    public final void open(int port, String ipAddress, CommonHandler handler)
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
        
        this.handler = handler;
        handler.loadPacketHandlers();
        reader = new ClientReader(this);
        reader.start();
        
        isRunning = true;
    }
    
    public final void close()
    {
        if(!isRunning)
        {
            handleException(new IllegalStateException("client is already closed"));
            return;
        }
        
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
}
