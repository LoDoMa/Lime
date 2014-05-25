package net.lodoma.lime.net.client;

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
