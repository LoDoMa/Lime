package net.lodoma.lime.server;

import java.io.IOException;
import java.util.Set;

import net.lodoma.lime.util.Identifiable;

public abstract class ServerPacket implements Identifiable<Integer>
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
        Set<ServerUser> users = server.userManager.getUserSet();
        for(ServerUser user : users)
            write(user, args);
    }
}
