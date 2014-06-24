package net.lodoma.lime.client.logic;

import net.lodoma.lime.client.generic.net.GenericClient;

public interface ClientLogic
{
    public void baseInit(GenericClient client);
    public void propertyInit();
    public void fetchInit();
    public void generalInit();
    
    public void clean();
    public void logic();
}
