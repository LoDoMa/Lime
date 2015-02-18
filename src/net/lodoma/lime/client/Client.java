package net.lodoma.lime.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import net.lodoma.lime.Lime;
import net.lodoma.lime.client.logic.CLGame;
import net.lodoma.lime.client.logic.ClientLogic;
import net.lodoma.lime.client.logic.ClientLogicThread;
import net.lodoma.lime.server.NetSettings;
import net.lodoma.lime.util.IdentityPool;
import net.lodoma.lime.util.Pipe;
import net.lodoma.lime.world.World;
import net.lodoma.lime.world.gfx.WorldRenderer;

public class Client
{
    private boolean isRunning = false;
    
    private Socket socket;
    
    private Pipe pipe;
    private InputStream socketInputStream;
    private OutputStream socketOutputStream;
    private DataInputStream publicInputStream;
    private DataOutputStream publicOutputStream;
    
    public ClientLogic logic;
    public ClientLogicThread logicThread;
    public ClientReader reader;
    
    public IdentityPool<ClientPacket> cpPool;
    public IdentityPool<ClientPacketHandler> cphPool;
    
    public World world;
    public WorldRenderer worldRenderer;
    
    public final void open(InetAddress address) throws ClientConnectionException
    {
        if(isRunning) return;
        
        try
        {
            socket = new Socket(address, NetSettings.PORT);

            pipe = new Pipe();
            socketInputStream = socket.getInputStream();
            socketOutputStream = socket.getOutputStream();
            publicInputStream = new DataInputStream(pipe.getInputStream());
            publicOutputStream = new DataOutputStream(socketOutputStream);
        }
        catch (IOException e)
        {
            Lime.LOGGER.W("Client failed to connect, throwing a ClientConnectionException");
            throw new ClientConnectionException(e);
        }
        
        cphPool = new IdentityPool<ClientPacketHandler>(true);
        cpPool = new IdentityPool<ClientPacket>(true);

        reader = new ClientReader(this, socketInputStream, pipe.getOutputStream());
        reader.start();

        logicThread = new ClientLogicThread(this, 60);
        logicThread.start();
        
        logic = new CLGame(this);
        
        isRunning = true;
    }
    
    public final void close()
    {
        if(!isRunning) return;

        logicThread.stop();
        reader.stop();
        
        try
        {
            pipe.close();
            socket.close();
        }
        catch (IOException e)
        {
            Lime.LOGGER.W("Failed to close socked and pipe");
            Lime.LOGGER.log(e);
        }
        
        isRunning = false;
        
        try
        {
            while(logicThread.isRunning()) Thread.sleep(1);
            while(reader.isRunning()) Thread.sleep(1);
        }
        catch(InterruptedException e)
        {
            Lime.LOGGER.W("Failed to stop logic pool and reader");
            Lime.LOGGER.log(e);
        }
    }
    
    public DataInputStream getInputStream()
    {
        return publicInputStream;
    }
    
    public DataOutputStream getOutputStream()
    {
        return publicOutputStream;
    }
    
    public boolean isRunning()
    {
        return isRunning;
    }
}
