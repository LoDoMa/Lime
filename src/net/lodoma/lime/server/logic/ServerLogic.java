package net.lodoma.lime.server.logic;

import net.lodoma.lime.server.Server;

public interface ServerLogic
{
    public void init(Server server);
    
    public void clean();
    
    public void logic();
}
