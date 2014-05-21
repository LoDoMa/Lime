package net.lodoma.lime.net.server.generic;

public abstract class ServerLogic extends Thread
{
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
