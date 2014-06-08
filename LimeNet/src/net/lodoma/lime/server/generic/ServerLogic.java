package net.lodoma.lime.server.generic;


public interface ServerLogic
{
    public void baseInit(GenericServer server);
    public void propertyInit();
    public void fetchInit();
    public void generalInit();
    
    public void clean();
    public void logic();
}
