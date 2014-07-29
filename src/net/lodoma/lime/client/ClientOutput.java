package net.lodoma.lime.client;

import java.io.DataOutputStream;
import java.io.IOException;

public abstract class ClientOutput
{
    protected Client client;
    protected DataOutputStream outputStream;
    
    private int hash;
    private Object[] expected;
    
    public ClientOutput(Client client, int hash, Object... expected)
    {
        this.client = client;
        outputStream = this.client.getOutputStream();
        
        this.hash = hash;
        this.expected = expected;
    }
    
    protected abstract void localHandle(Object... args) throws IOException;
    
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
            client.setCloseMessage("Server closed (output exception)");
            client.closeInThread();
        }
    }
}
