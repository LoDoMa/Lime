package net.lodoma.lime.client.logic;

import net.lodoma.lime.client.Client;

public interface ClientLogic
{
    public void init(Client client);
    
    public void clean();
    
    public void logic();
}
