package net.lodoma.lime.server.logic;

import net.lodoma.lime.server.generic.GenericServer;

public interface ServerLogic
{
    public void baseInit(GenericServer server);
    public void propertyInit();
    public void fetchInit();
    public void generalInit();
    
    public void clean();
    public void logic();
}
