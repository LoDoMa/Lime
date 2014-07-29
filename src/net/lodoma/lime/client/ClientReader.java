package net.lodoma.lime.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedOutputStream;

import net.lodoma.lime.util.HashPool32;

public class ClientReader implements Runnable
{
    private Client client;
    
    private Thread thread;
    private boolean running;
    
    private InputStream privateInputStream;
    private PipedOutputStream privateOutputStream;
    
    private DataInputStream inputStream;
    
    private HashPool32<ClientInputHandler> cihPool;
    
    @SuppressWarnings("unchecked")
    public ClientReader(Client client)
    {
        this.client = client;
        
        privateInputStream = client.privateInputStream;
        privateOutputStream = client.privateOutputStream;
        
        inputStream = client.getInputStream();
        
        cihPool = (HashPool32<ClientInputHandler>) client.getProperty("cihPool");
    }
    
    public void start()
    {
        if(running) return;
        running = true;
        thread = new Thread(this, "ClientReaderThread");
        thread.start();
    }
    
    public void stop()
    {
        if(!running) return;
        running = false;
    }
    
    public boolean isRunning()
    {
        return thread.isAlive();
    }
    
    public void handleInput() throws IOException
    {
        while(inputStream.available() >= 8)
        {
            int hash = inputStream.readInt();
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
                if(!running) break;
                client.setCloseMessage("Server closed (reader exception)");
                client.closeInThread();
                break;
            }
        }
    }
}
