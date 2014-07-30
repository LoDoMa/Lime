package net.lodoma.lime.client;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * ClientPacketHandler handles input from the server starting with
 * a specified 32-bit hash. The packet handler should always expect
 * data in the same format and should be used on only one hash.
 * CPH is often used instead of ClientPacketHandler. Names of classes
 * that extend ClientPacketHandler should start with "CPH".
 * 
 * @author Lovro Kalinovčić
 */
public abstract class ClientPacketHandler
{
    private static final String FAILURE_CLOSE_MESSAGE = "Server closed (packet handler exception)";
    
    protected Client client;                    // the client that uses this handler
    protected DataInputStream inputStream;      // input stream from the server
    
    public ClientPacketHandler(Client client)
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
     * Handles the input from server. Closes the client
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
