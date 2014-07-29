package net.lodoma.lime.server;

import java.io.IOException;
import java.util.List;

import net.lodoma.lime.server.logic.UserManager;

public abstract class ServerOutput
{
    protected Server server;
    private int hash;
    private Class<?>[] expected;
    
    public ServerOutput(Server server, int hash, Class<?>... expected)
    {
        this.server = server;
        this.hash = hash;
        this.expected = expected;
    }
    
    protected abstract void localHandle(ServerUser user, Object... args) throws IOException;
    
    public final void handle(ServerUser user, Object... args)
    {
        try
        {
            user.outputStream.writeInt(hash);
            
            if(expected.length != args.length)
                throw new IllegalArgumentException();
            for(int i = 0; i < expected.length; i++)
                if(args[i].getClass().isInstance(expected[i]))
                    throw new IllegalArgumentException();
            
            localHandle(user, args);
            
            user.outputStream.flush();
        }
        catch (IOException e)
        {
            user.closed();
        }
    }
    
    public final void handleAll(Object... args)
    {
        UserManager userManager = ((UserManager) server.getProperty("userManager"));
        List<ServerUser> users = userManager.getUserList();
        for(ServerUser user : users)
            handle(user, args);
    }
}
