package net.lodoma.lime.server;

import java.io.IOException;
import java.util.Set;

import net.lodoma.lime.server.logic.UserManager;

public abstract class ServerPacket
{
    protected Server server;
    private int hash;
    private Class<?>[] expected;
    
    public ServerPacket(Server server, int hash, Class<?>... expected)
    {
        this.server = server;
        this.hash = hash;
        this.expected = expected;
    }
    
    protected abstract void localWrite(ServerUser user, Object... args) throws IOException;
    
    public final void write(ServerUser user, Object... args)
    {
        try
        {
            user.outputStream.writeInt(hash);
            
            if(expected.length != args.length)
                throw new IllegalArgumentException();
            for(int i = 0; i < expected.length; i++)
                if(!expected[i].isInstance(args[i]))
                    throw new IllegalArgumentException();
            
            localWrite(user, args);
            
            user.outputStream.flush();
        }
        catch (IOException e)
        {
            user.closed = true;
        }
    }
    
    public final void handleAll(Object... args)
    {
        UserManager userManager = ((UserManager) server.getProperty("userManager"));
        Set<ServerUser> users = userManager.getUserSet();
        for(ServerUser user : users)
            write(user, args);
    }
}
