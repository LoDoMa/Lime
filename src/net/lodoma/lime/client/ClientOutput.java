package net.lodoma.lime.client;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * ClientOutput writes output to the server of any format starting with
 * a specified 32-bit hash. The output should always be in the same format.
 * CO is often used instead of ClientOutput. Names of classes
 * that extend ClientOutput should start with "CO".
 * 
 * @author Lovro Kalinovčić
 */
public abstract class ClientOutput
{
    private static final String FAILURE_CLOSE_MESSAGE = "Server closed (output exception)";
    
    protected Client client;                    // the client that uses this output
    protected DataOutputStream outputStream;    // output stream to server
    
    private int hash;               // 32-bit hash at the start of the packet.
    private Object[] expected;      // expected arguments to the handle function
    
    /**
     * 
     * @param client - the client that uses this output
     * @param hash - 32-bit hash at the start of the packet
     * @param expected - expected arguments to the handle function
     */
    public ClientOutput(Client client, int hash, Object... expected)
    {
        this.client = client;
        outputStream = this.client.getOutputStream();
        
        this.hash = hash;
        this.expected = expected;
    }
    
    /**
     * Writes to the output stream.
     * 
     * @param args - given arguments
     * @throws IOException if writing to the output stream fails
     */
    protected abstract void localHandle(Object... args) throws IOException;
    
    /**
     * Writes to the output stream. Closes the client
     * if an IOException is thrown.
     * 
     * @param args - given arguments
     */
    public final void handle(Object... args)
    {
        try
        {
            outputStream.writeInt(hash);
            
            if(expected.length != args.length)
                throw new IllegalArgumentException();
            for(int i = 0; i < expected.length; i++)
                if(args[i].getClass() != expected[i])
                    throw new IllegalArgumentException();
            
            localHandle(args);
            
            outputStream.flush();
        }
        catch (IOException e)
        {
            client.setCloseMessage(FAILURE_CLOSE_MESSAGE);
            client.closeInThread();
        }
    }
}
