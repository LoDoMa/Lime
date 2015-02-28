package net.lodoma.lime.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.Socket;

import net.lodoma.lime.input.InputData;
import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.world.WorldSnapshot;
import net.lodoma.lime.world.gfx.Camera;

// NOTE: This class requires more neatness!

public final class ServerUser implements Runnable, Identifiable<Integer>
{
    public Server server;
    public Socket socket;
    
    private InputStream privateInputStream;
    private PipedOutputStream privateOutputStream;
    
    public DataInputStream inputStream;
    public DataOutputStream outputStream;
    
    public Thread thread;
    public boolean running;
    
    public boolean closed;
    
    public int identifier;
    
    public WorldSnapshot lastSnapshot;
    public Camera camera;
    public InputData inputData;
    
    public ServerUser(Socket socket, Server server)
    {
        this.server = server;
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
        
        lastSnapshot = null;
        camera = new Camera();
        inputData = new InputData();
    }
    
    @Override
    public Integer getIdentifier()
    {
        return identifier;
    }
    
    @Override
    public void setIdentifier(Integer identifier)
    {
        this.identifier = identifier;
        inputData.userID = identifier;
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
    
    public void handleInput() throws IOException
    {
        while(inputStream.available() >= 4)
        {
            int hash = inputStream.readInt();
            ServerPacketHandler handler = server.sphPool.get(hash);
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
        
        closed = true;
    }
}
