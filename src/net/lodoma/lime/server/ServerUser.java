package net.lodoma.lime.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.Socket;

import net.lodoma.lime.security.Credentials;
import net.lodoma.lime.util.HashPool32;

public final class ServerUser implements Runnable
{
    private HashPool32<ServerPacketHandler> sphPool;
    
    private Socket socket;
    
    private InputStream privateInputStream;
    private PipedOutputStream privateOutputStream;
    
    public DataInputStream inputStream;
    public DataOutputStream outputStream;
    
    private Thread thread;
    private boolean running;
    
    private boolean isClosed;
    
    private Credentials credentials;
    private int ID;
    
    @SuppressWarnings("unchecked")
    public ServerUser(Socket socket, Server server)
    {
        sphPool = (HashPool32<ServerPacketHandler>) server.getProperty("sphPool");
        
        this.socket = socket;
        try
        {
            privateInputStream = this.socket.getInputStream();
            outputStream = new DataOutputStream(this.socket.getOutputStream());
            
            privateOutputStream = new PipedOutputStream();
            inputStream = new DataInputStream(new PipedInputStream(privateOutputStream));
            
            credentials = new Credentials(privateInputStream);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
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
    
    public Credentials getCredentials()
    {
        return credentials;
    }
    
    public int getID()
    {
        return ID;
    }
    
    public void setID(int id)
    {
        ID = id;
    }
    
    public void close()
    {
        isClosed = true;
    }
    
    public boolean isClosed()
    {
        return isClosed;
    }
    
    public void handleInput() throws IOException
    {
        while(inputStream.available() >= 8)
        {
            int hash = inputStream.readInt();
            ServerPacketHandler handler = sphPool.get(hash);
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
        
        close();
    }
}
