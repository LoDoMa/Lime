package net.lodoma.lime.server;

import java.io.IOException;

import net.lodoma.lime.common.net.NetStage;

public abstract class ServerInputHandler
{
    protected Server server;
    private NetStage[] stages;
    
    public ServerInputHandler(Server server, NetStage... stages)
    {
        this.server = server;
        this.stages = stages;
    }
    
    protected abstract void localHandle(ServerUser user) throws IOException;
    
    public synchronized final void handle(ServerUser user)
    {
        for(int i = 0; i < stages.length; i++)
        {
            if(user.stage == stages[i]) break;
            if(i == stages.length - 1) return;
        }
        try
        {
            localHandle(user);
        }
        catch (IOException e)
        {
            user.socketClosed();
        }
    }
}
