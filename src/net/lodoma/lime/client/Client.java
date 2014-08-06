package net.lodoma.lime.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import net.lodoma.lime.client.logic.CLBase;
import net.lodoma.lime.client.logic.CLChat;
import net.lodoma.lime.client.logic.CLWorld;
import net.lodoma.lime.client.logic.ClientLogicPool;
import net.lodoma.lime.common.PropertyPool;
import net.lodoma.lime.event.EventManager;
import net.lodoma.lime.security.Credentials;
import net.lodoma.lime.util.HashPool32;
import net.lodoma.lime.util.Pipe;

public class Client implements PropertyPool
{
    private boolean isRunning = false;
    private String closeMessage;
    
    private Socket socket;
    
    private Pipe pipe;
    private InputStream socketInputStream;
    private OutputStream socketOutputStream;
    private DataInputStream publicInputStream;
    private DataOutputStream publicOutputStream;

    private ClientLogicPool logicPool;
    private ClientReader reader;
    
    private Map<String, Object> properties;
    
    public final void open(int port, String host, Credentials credentials) throws ClientConnectionException
    {
        if(isRunning) return;
        
        try
        {
            socket = new Socket(host, port);

            pipe = new Pipe();
            socketInputStream = socket.getInputStream();
            socketOutputStream = socket.getOutputStream();
            publicInputStream = new DataInputStream(pipe.getInputStream());
            publicOutputStream = new DataOutputStream(socketOutputStream);
            
            credentials.write(publicOutputStream);
        }
        catch (IOException e)
        {
            throw new ClientConnectionException(e);
        }
        
        logicPool = new ClientLogicPool(this, 60.0);
        reader = new ClientReader(this, socketInputStream, pipe.getOutputStream());
        
        properties = new HashMap<String, Object>();

        setProperty("logicPool", logicPool);
        setProperty("reader", reader);
        setProperty("credentials", credentials);
        
        setProperty("cphPool", new HashPool32<ClientPacketHandler>());
        setProperty("cpPool", new HashPool32<ClientPacket>());
        setProperty("emanPool", new HashPool32<EventManager>());
        
        logicPool.addLogic(new CLBase());
        logicPool.addLogic(new CLChat());
        logicPool.addLogic(new CLWorld());
        
        logicPool.init();
        
        reader.start();
        logicPool.start();
        
        closeMessage = null;
        isRunning = true;
    }
    
    public final void close()
    {
        if(!isRunning) return;

        logicPool.stop();
        reader.stop();
        
        try
        {
            socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        isRunning = false;
        
        try
        {
            while(logicPool.isRunning()) Thread.sleep(1);
            while(reader.isRunning()) Thread.sleep(1);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    
    public void closeInThread()
    {
        if(!isRunning) return;
        
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                close();
            }
        }, "ClientCloseThread").start();
    }
    
    public String getCloseMessage()
    {
        return closeMessage;
    }
    
    public void setCloseMessage(String closeMessage)
    {
        this.closeMessage = closeMessage;
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
    
    @Override
    public Object getProperty(String name)
    {
        return properties.get(name);
    }

    @Override
    public void setProperty(String name, Object value)
    {
        properties.put(name, value);
    }

    @Override
    public boolean hasProperty(String name)
    {
        return properties.containsKey(name);
    }

    @Override
    public void removeProperty(String name)
    {
        properties.remove(name);
    }
}
