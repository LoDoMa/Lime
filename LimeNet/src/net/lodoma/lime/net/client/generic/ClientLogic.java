package net.lodoma.lime.net.client.generic;

public abstract class ClientLogic extends Thread
{
    protected GenericClient client;
    
    void setClient(GenericClient client)
    {
        this.client = client;
    }
    
    public abstract void onOpen();
    public abstract void onClose();
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
