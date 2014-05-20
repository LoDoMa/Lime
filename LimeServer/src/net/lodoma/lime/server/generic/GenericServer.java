package net.lodoma.lime.server.generic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import net.lodoma.lime.packet.generic.CommonHandler;

public abstract class GenericServer
{
    private boolean isRunning = false;
    
    DatagramSocket socket;
    
    UserPool userPool;
    CommonHandler handler;
    ServerReader reader;
    
    public abstract void handleException(Exception exception);
    
    public final void open(int port, CommonHandler handler)
    {
        if (isRunning)
        {
            handleException(new IllegalStateException("server is already open"));
            return;
        }
        
        try
        {
            socket = new DatagramSocket(port);
        }
        catch (SocketException e)
        {
            handleException(e);
        }
        
        userPool = new UserPool();
        this.handler = handler;
        handler.loadPacketHandlers();
        reader = new ServerReader(this);
        reader.start();
        
        isRunning = true;
    }
    
    public final void close()
    {
        if (!isRunning)
        {
            handleException(new IllegalStateException("server is already closed"));
            return;
        }
        
        reader.interrupt();
        socket.close();
        
        isRunning = false;
    }
    
    public final void sendData(byte[] data, ServerUser user)
    {
        if (!isRunning)
        {
            handleException(new IllegalStateException("cannot send data while closed"));
            return;
        }
        
        DatagramPacket packet = new DatagramPacket(data, data.length, user.address, user.port);
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
