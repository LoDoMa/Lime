package net.lodoma.lime.server;

import java.io.IOException;

import net.lodoma.lime.util.Identifiable;

public abstract class ServerPacketHandler implements Identifiable<Integer>
{
    protected Server server;
    
    private int hash;
    
    public ServerPacketHandler(Server server, int hash)
    {
        this.server = server;
        this.hash = hash;
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
    
    protected abstract void localHandle(ServerUser user) throws IOException;
    
    public synchronized final void handle(ServerUser user)
    {
        try
        {
            localHandle(user);
        }
        catch (IOException e)
        {
            user.closed = true;
        }
    }
}
