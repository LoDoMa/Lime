package net.lodoma.lime.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedOutputStream;

import net.lodoma.lime.util.HashPool;

public class ClientReader implements Runnable
{
    private Thread thread;
    private boolean running;
    
    private InputStream privateInputStream;
    private PipedOutputStream privateOutputStream;
    
    private DataInputStream inputStream;
    
    private HashPool<ClientInputHandler> cihPool;
    
    @SuppressWarnings("unchecked")
    public ClientReader(Client client)
    {
        privateInputStream = client.privateInputStream;
        privateOutputStream = client.privateOutputStream;
        
        inputStream = client.getInputStream();
        
        cihPool = (HashPool<ClientInputHandler>) client.getProperty("cihPool");
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
    
    public void handleInput() throws IOException
    {
        while(inputStream.available() >= 8)
        {
            long hash = inputStream.readLong();
            ClientInputHandler handler = cihPool.get(hash);
            if(handler != null)
                handler.handle();
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
                if(readByte == -1)
                    throw new IOException();
                privateOutputStream.write(readByte);
                privateOutputStream.flush();
            }
            catch(IOException e)
            {
                stop();
                break;
            }
        }
    }
}
