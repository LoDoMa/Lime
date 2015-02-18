package net.lodoma.lime.client.logic;

import java.io.DataInputStream;

import net.lodoma.lime.client.Client;

public abstract class ClientLogic
{
    protected Client client;
    
    public ClientLogic(Client client)
    {
        this.client = client;
    }

    public abstract void init();
    public abstract void destroy();

    public abstract void update();
    public abstract void render();
    
    public abstract void handleSnapshot(DataInputStream inputStream);
}
