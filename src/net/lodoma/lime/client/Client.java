package net.lodoma.lime.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import net.lodoma.lime.client.logic.CLWorld;
import net.lodoma.lime.client.logic.ClientLogicPool;
import net.lodoma.lime.common.PropertyPool;
import net.lodoma.lime.event.EventManager;
import net.lodoma.lime.security.Credentials;
import net.lodoma.lime.util.HashPool32;
import net.lodoma.lime.util.Pipe;

/**
 * Client holds all data for communication with the server.
 * The cilent tries to establish a connection with the server
 * when it is opened, and has to be closed for this connection
 * to properly end.
 * 
 * When opened, ClientReader and ClientLogicPool threads are
 * started.
 * 
 * The client is also a PropertyPool for sharing objects
 * between client logic components.
 * 
 * @author Lovro Kalinovčić
 */
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
    
    /**
     * Opens the client if not running.
     * First tries to establish a connection with the server,
     * then initializes the client before starting its logic.
     * 
     * A ClientLogicPool is created, with UPS set to 60.
     * The property pool is created.
     * The following properties are set:
     *     logicPool, reader, credentials,
     *     cphPool, cpPool, emanPool
     * 
     * ClientLogicPool and ClientReader threads are started.
     * 
     * @param port - server port
     * @param host - server host
     * @param credentials - user credentials
     * @throws ClientConnectionException is establishing connection fails.
     */
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
        
        logicPool.addLogic(new CLWorld());
        
        logicPool.init();
        
        reader.start();
        logicPool.start();
        
        closeMessage = null;
        isRunning = true;
    }
    
    /**
     * Closes the client is running.
     * First stops ClientLogicPool and ClientReader threads,
     * then tries to close the client socket.
     * 
     * This method will wait until both ClientLogicPool
     * and ClientReader threads have stopped.
     */
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
    
    /**
     * Creates a new thread and starts it.
     * That thread then closes the server.
     * The created thread is named "ClientCloseThread".
     */
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
    
    /**
     * @return the close message.
     */
    public String getCloseMessage()
    {
        return closeMessage;
    }
    
    /**
     * Sets the close message.
     * @param closeMessage - new close message
     */
    public void setCloseMessage(String closeMessage)
    {
        this.closeMessage = closeMessage;
    }
    
    /**
     * Returns the input stream.
     * This stream is not the socket input stream,
     * but piped output from ClientReader.
     * 
     * @return input stream
     */
    public DataInputStream getInputStream()
    {
        return publicInputStream;
    }
    
    /**
     * Returns the output stream.
     * This stream is the socket output stream.
     * 
     * @return output stream.
     */
    public DataOutputStream getOutputStream()
    {
        return publicOutputStream;
    }
    
    /**
     * @return is the client running
     */
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
