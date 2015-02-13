package net.lodoma.lime.client;

import java.io.DataOutputStream;
import java.io.IOException;

import net.lodoma.lime.Lime;
import net.lodoma.lime.util.Identifiable;

/**
 * ClientPacket writes output to the server of any format starting with
 * a specified 32-bit hash. The output should always be in the same format.
 * CP is often used instead of ClientPacket. Names of classes
 * that extend ClientPacket should start with "CP".
 * 
 * @author Lovro Kalinovčić
 */
public abstract class ClientPacket implements Identifiable<Integer>
{
    protected Client client;                    // the client that uses this output
    protected DataOutputStream outputStream;    // output stream to server
    
    private int hash;               // 32-bit hash at the start of the packet.
    private Class<?>[] expected;    // expected arguments to the handle function
    
    /**
     * 
     * @param client - the client that uses this packet
     * @param hash - 32-bit hash at the start of the packet
     * @param expected - expected arguments to the handle function
     */
    public ClientPacket(Client client, int hash, Class<?>... expected)
    {
        this.client = client;
        outputStream = this.client.getOutputStream();
        
        this.hash = hash;
        this.expected = expected;
    }
    
    @Override
    public Integer getIdentifier()
    {
        return hash;
    }
    
    @Override
    public void setIdentifier(Integer identifier)
    {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Writes to the output stream.
     * 
     * @param args - given arguments
     * @throws IOException if writing to the output stream fails
     */
    protected abstract void localWrite(Object... args) throws IOException;
    
    /**
     * Writes to the output stream. Closes the client
     * if an IOException is thrown.
     * 
     * @param args - given arguments
     */
    public final void write(Object... args)
    {
        try
        {
            outputStream.writeInt(hash);
            
            if (expected.length != args.length)
                throw new IllegalArgumentException();
            for (int i = 0; i < expected.length; i++)
                if (!expected[i].isInstance(args[i]))
                    throw new IllegalArgumentException();
            
            localWrite(args);
            
            outputStream.flush();
        }
        catch (IOException e)
        {
            Lime.LOGGER.C("IO exception while sending a packet");
            Lime.LOGGER.log(e);
            Lime.forceExit();
        }
    }
}
