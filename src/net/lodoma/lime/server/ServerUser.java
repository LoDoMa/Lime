package net.lodoma.lime.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.Socket;

import net.lodoma.lime.common.NetStage;
import net.lodoma.lime.util.HashPool;
import net.lodoma.lime.util.SystemHelper;

public final class ServerUser implements Runnable
{
    public NetStage stage;
    
    private HashPool<ServerInputHandler> sihPool;
    
    private Socket socket;
    
    private InputStream privateInputStream;
    private PipedOutputStream privateOutputStream;
    
    public DataInputStream inputStream;
    public DataOutputStream outputStream;
    
    private Thread thread;
    private boolean running;
    
    private long lastResponseTime;
    
    @SuppressWarnings("unchecked")
    public ServerUser(NetStage stage, Socket socket, Server server)
    {
        this.stage = stage;
        
        sihPool = (HashPool<ServerInputHandler>) server.getProperty("sihPool");
        
        this.socket = socket;
        try
        {
            privateInputStream = this.socket.getInputStream();
            outputStream = new DataOutputStream(this.socket.getOutputStream());
            
            privateOutputStream = new PipedOutputStream();
            inputStream = new DataInputStream(new PipedInputStream(privateOutputStream));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        lastResponseTime = SystemHelper.getTimeNanos();
    }
    
    public void start()
    {
        if(running) return;
        running = true;
        thread = new Thread(this, "ServerUserThread");
        thread.start();
    }
    
    public void stop()
    {
        if(!running) return;
        running = false;
        try
        {
            socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public long getLastResponseTime()
    {
        return lastResponseTime;
    }
    
    public void closed()
    {
        lastResponseTime = 0;
    }
    
    public void handleInput() throws IOException
    {
        while(inputStream.available() >= 8)
        {
            long hash = inputStream.readLong();
            ServerInputHandler handler = sihPool.get(hash);
            if(handler != null)
                handler.handle(this);
        }
    }
    
    @Override
    public void run()
    {
        while(running)
        {
            try
            {
                int readByte = privateInputStream.read();
                if(readByte == -1) break;
                lastResponseTime = System.nanoTime();
                privateOutputStream.write(readByte);
                privateOutputStream.flush();
            }
            catch(IOException e)
            {
                break;
            }
        }
        
        if(!socket.isClosed())
            try
            {
                socket.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        
        closed();
    }
}
