package net.lodoma.lime.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import net.lodoma.lime.client.logic.CLBase;
import net.lodoma.lime.client.logic.CLChat;
import net.lodoma.lime.client.logic.CLWorld;
import net.lodoma.lime.client.logic.ClientLogicPool;
import net.lodoma.lime.util.HashPool;

public class Client
{
    private boolean isRunning = false;
    
    private Socket socket;
    
    InputStream privateInputStream;
    PipedOutputStream privateOutputStream;

    private DataInputStream inputStream;
    private DataOutputStream outputStream;
   
    private ClientReader reader;
    
    private ClientLogicPool logicPool;
    private Map<String, Object> properties;
    
    public final void open(int port, String host)
    {
        if(isRunning)
            throw new IllegalStateException("client is already open");
        
        try
        {
            socket = new Socket(host, port);
            
            privateInputStream = socket.getInputStream();
            privateOutputStream = new PipedOutputStream();

            inputStream = new DataInputStream(new PipedInputStream(privateOutputStream));
            outputStream = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        logicPool = new ClientLogicPool(this);
        properties = new HashMap<String, Object>();
        
        setProperty("cihPool", new HashPool<ClientInputHandler>());
        reader = new ClientReader(this);
        setProperty("coPool", new HashPool<ClientOutput>());
        setProperty("reader", reader);
        
        logicPool.addLogic(new CLBase());
        logicPool.addLogic(new CLChat());
        logicPool.addLogic(new CLWorld());
        
        logicPool.init();
        
        reader.start();
        isRunning = true;
        logicPool.start();
    }
    
    public final void close()
    {
        if(!isRunning)
            throw new IllegalStateException("client is already closed");

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
    }
    
    public DataInputStream getInputStream()
    {
        return inputStream;
    }
    
    public DataOutputStream getOutputStream()
    {
        return outputStream;
    }
    
    public boolean isRunning()
    {
        return isRunning;
    }
    
    public Object getProperty(String name)
    {
        return properties.get(name);
    }
    
    public void setProperty(String name, Object value)
    {
        properties.put(name, value);
    }
    
    public boolean hasProperty(String name)
    {
        return properties.containsKey(name);
    }
    
    public void removeProperty(String name)
    {
        properties.remove(name);
    }
}
