package net.lodoma.lime.server;

import java.io.IOException;

public abstract class ServerPacketHandler
{
    protected Server server;
    
    public ServerPacketHandler(Server server)
    {
        this.server = server;
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
            user.close();
        }
    }
}
