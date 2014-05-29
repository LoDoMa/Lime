package net.lodoma.lime.client.net;

public class VisualInstance
{
    private LimeClient client;
    
    public VisualInstance()
    {
        client = new LimeClient();
    }
    
    public void start()
    {
        client.open(19523, "localhost", new LimeClientLogic());
    }
}
