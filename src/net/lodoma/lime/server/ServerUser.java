package net.lodoma.lime.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import net.lodoma.lime.common.net.NetStage;
import net.lodoma.lime.util.HashPool;
import net.lodoma.lime.util.SystemHelper;

public final class ServerUser implements Runnable
{
    public NetStage stage;
    
    private Server server;
    private HashPool<ServerInputHandler> sihPool;
    
    private Socket socket;
    public DataInputStream inputStream;
    public DataOutputStream outputStream;
    
    private Thread thread;
    private boolean running;
    
    private long lastResponseTime;
    
    @SuppressWarnings("unchecked")
    public ServerUser(NetStage stage, Socket socket, Server server)
    {
        this.stage = stage;
        
        this.server = server;
        this.sihPool = (HashPool<ServerInputHandler>) this.server.getProperty("sihPool");
        
        this.socket = socket;
        try
        {
            inputStream = new DataInputStream(this.socket.getInputStream());
            outputStream = new DataOutputStream(this.socket.getOutputStream());
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
        thread = new Thread(this);
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
    
    @Override
    public void run()
    {
        while(running)
        {
            try
            {
                long hash = inputStream.readLong();
                lastResponseTime = SystemHelper.getTimeNanos();
                sihPool.get(hash).handle(this);
            }
            catch(IOException e)
            {
                if(running)
                {
                    running = false;
                    break;
                }
            }
        }
        
        if(!socket.isClosed())
        {
            try
            {
                socket.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
