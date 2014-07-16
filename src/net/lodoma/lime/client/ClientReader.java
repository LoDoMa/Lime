package net.lodoma.lime.client;

import java.io.DataInputStream;
import java.io.IOException;

import net.lodoma.lime.util.HashPool;

public class ClientReader implements Runnable
{
    private Thread thread;
    private boolean running;
    
    private Client client;
    private DataInputStream inputStream;
    
    private HashPool<ClientInputHandler> cihPool;
    
    @SuppressWarnings("unchecked")
    public ClientReader(Client client)
    {
        this.client = client;
        inputStream = this.client.getInputStream();
        cihPool = (HashPool<ClientInputHandler>) this.client.getProperty("cihPool");
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
    }
    
    @Override
    public void run()
    {
        while(running)
        {
            try
            {
                long hash = inputStream.readLong();
                cihPool.get(hash).handle();
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
    }
}
