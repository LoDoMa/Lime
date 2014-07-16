package net.lodoma.lime.client.logic;

import net.lodoma.lime.client.Client;

public interface ClientLogic
{
    public void baseInit(Client client);
    public void propertyInit();
    public void fetchInit();
    public void generalInit();
    
    public void clean();
    public void logic();
}
