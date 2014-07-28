package net.lodoma.lime.client;

import java.io.DataInputStream;
import java.io.IOException;

public abstract class ClientInputHandler
{
    protected Client client;
    protected DataInputStream inputStream;
    
    public ClientInputHandler(Client client)
    {
        this.client = client;
        inputStream = this.client.getInputStream();
    }
    
    protected abstract void localHandle() throws IOException;
    
    public final void handle()
    {
        try
        {
            localHandle();
        }
        catch(IOException e)
        {
            client.setCloseMessage("Server closed (input handler exception)");
            client.closeInThread();
        }
    }
}
