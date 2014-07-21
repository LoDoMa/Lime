package net.lodoma.lime.server;

import java.io.IOException;
import java.net.SocketException;

import net.lodoma.lime.util.HashHelper;

public abstract class ServerOutput
{
    protected Server server;
    private long hash;
    private Class<?>[] expected;
    
    public ServerOutput(Server server, String soName, Class<?>... expected)
    {
        this.server = server;
        hash = HashHelper.hash64(soName);
        this.expected = expected;
    }
    
    protected abstract void localHandle(ServerUser user, Object... args) throws IOException;
    
    public final void handle(ServerUser user, Object... args)
    {
        try
        {
            user.outputStream.writeLong(hash);
            
            if(expected.length != args.length)
                throw new IllegalArgumentException();
            for(int i = 0; i < expected.length; i++)
                if(args[i].getClass() != expected[i])
                    throw new IllegalArgumentException();
            
            localHandle(user, args);
            
            user.outputStream.flush();
        }
        catch (IOException e)
        {
            if(e instanceof SocketException)
                user.closed();
            else
                e.printStackTrace();
        }
    }
}
