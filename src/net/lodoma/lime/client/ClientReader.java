package net.lodoma.lime.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * ClientReader is a thread that handles input from the server.
 * Generally, it reads from an InputStream and writes data to
 * an OutputStream.
 * 
 * This InputStream is usually provided by socket, and the
 * OutputStream is usually piped.
 * 
 * @author Lovro Kalinovčić
 */
public class ClientReader implements Runnable
{   
    private Thread thread;
    private boolean running;
    
    private Client client;

    private InputStream inputStream;
    private OutputStream outputStream;
    
    /**
     * 
     * @param client - the client that uses this reader
     * @param inputStream - input stream to read from
     * @param outputStream - output stream to write to
     */
    public ClientReader(Client client, InputStream inputStream, OutputStream outputStream)
    {
        this.client = client;
        
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }
    
    /**
     * Starts the thread if not running.
     * The thread is named "ClientReaderThread".
     */
    public void start()
    {
        if(running) return;
        running = true;
        thread = new Thread(this, "ClientReaderThread");
        thread.start();
    }
    
    /**
     * Stops the thread if running.
     */
    public void stop()
    {
        if(!running) return;
        running = false;
    }
    
    /**
     * Tests if the thread is running.
     * 
     * @return is the thread running.
     */
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
                    byte[] bytes = new byte[1024];
                    int amount = inputStream.read(bytes);
                    outputStream.write(bytes, 0, amount);
                }
                Thread.sleep(1);
            }
            catch(IOException | InterruptedException e)
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
