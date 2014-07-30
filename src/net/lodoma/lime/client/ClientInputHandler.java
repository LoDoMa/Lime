package net.lodoma.lime.client;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * ClientInputHandler handles input from the server starting with
 * a specified 32-bit hash. The input handler should always expect
 * data in the same format and should be used on only one hash.
 * CIH is often used instead of ClientInputHandler. Names of classes
 * that extend ClientInputHandler should start with "CIH".
 * 
 * @author Lovro Kalinovčić
 */
public abstract class ClientInputHandler
{
    private static final String FAILURE_CLOSE_MESSAGE = "Server closed (input handler exception)";
    
    protected Client client;                    // the client that uses this handler
    protected DataInputStream inputStream;      // input stream from the server
    
    public ClientInputHandler(Client client)
    {
        this.client = client;
        inputStream = this.client.getInputStream();
    }
    
    /**
     * Handles the input from server.
     * @throws IOException if reading from input stream fails.
     */
    protected abstract void localHandle() throws IOException;
    
    /**
     * Handles the input from server. Closes the server
     * if an IOException is thrown. 
     */
    public final void handle()
    {
        try
        {
            localHandle();
        }
        catch(IOException e)
        {
            client.setCloseMessage(FAILURE_CLOSE_MESSAGE);
            client.closeInThread();
        }
    }
}
