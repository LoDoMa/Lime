package net.lodoma.lime.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ClientReader implements Runnable
{   
    private Thread thread;
    private boolean running;
    
    private Client client;

    private InputStream inputStream;
    private OutputStream outputStream;
    
    public ClientReader(Client client, InputStream inputStream, OutputStream outputStream)
    {
        this.client = client;
        
        this.inputStream = inputStream;
        this.outputStream = outputStream;
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
    
    @Override
    public void run()
    {
        while(running)
        {
            try
            {
                if(inputStream.available() > 0)
                {
                    byte[] bytes = new byte[Math.min(1024, inputStream.available())];
                    inputStream.read(bytes);
                    outputStream.write(bytes);
                }
            }
            catch(IOException e)
            {
                if(!running) break;
                e.printStackTrace();
                client.setCloseMessage("Server closed (reader exception)");
                client.closeInThread();
                break;
            }
        }
    }
}
