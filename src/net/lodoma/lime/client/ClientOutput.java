package net.lodoma.lime.client;

import java.io.DataOutputStream;
import java.io.IOException;

import net.lodoma.lime.util.HashHelper;

public abstract class ClientOutput
{
    protected Client client;
    protected DataOutputStream outputStream;
    
    private long hash;
    private Object[] expected;
    
    public ClientOutput(Client client, String coName, Object... expected)
    {
        this.client = client;
        outputStream = this.client.getOutputStream();
        
        hash = HashHelper.hash64(coName);
        this.expected = expected;
    }
    
    protected abstract void localHandle(Object... args) throws IOException;
    
    public synchronized final void handle(Object... args)
    {
        try
        {
            outputStream.writeLong(hash);
            
            if(expected.length != args.length)
                throw new IllegalArgumentException();
            for(int i = 0; i < expected.length; i++)
                if(args[i].getClass() != expected[i])
                    throw new IllegalArgumentException();
            
            localHandle(args);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}