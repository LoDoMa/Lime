package net.lodoma.lime.server.logic;

import net.lodoma.lime.server.Server;

public interface ServerLogic
{
    public void baseInit(Server server);
    public void propertyInit();
    public void fetchInit();
    public void generalInit();
    
    public void clean();
    public void logic();
}
