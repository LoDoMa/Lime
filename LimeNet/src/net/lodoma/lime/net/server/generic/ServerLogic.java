package net.lodoma.lime.net.server.generic;

public abstract class ServerLogic extends Thread
{
    protected GenericServer server;
    
    void setServer(GenericServer server)
    {
        this.server = server;
    }
    
    public abstract void logic();
    
    @Override
    public void run()
    {
        while(!isInterrupted())
        {
            logic();
        }
    }
}
