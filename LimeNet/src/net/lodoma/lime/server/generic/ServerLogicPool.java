package net.lodoma.lime.server.generic;

import java.util.HashSet;
import java.util.Set;

public class ServerLogicPool implements Runnable
{
    private GenericServer server;
    
    private boolean running = false;
    private Thread thread;
    
    private Set<ServerLogic> logicSet;
    
    public ServerLogicPool(GenericServer server)
    {
        this.server = server;
        logicSet = new HashSet<ServerLogic>();
    }
    
    void init()
    {
        for(ServerLogic logic : logicSet)
            logic.baseInit(server);
        for(ServerLogic logic : logicSet)
            logic.propertyInit();
        for(ServerLogic logic : logicSet)
            logic.fetchInit();
        for(ServerLogic logic : logicSet)
            logic.generalInit();
    }
    
    void start()
    {
        if(running) return;
        running = true;
        
        thread = new Thread(this);
        thread.start();
    }
    
    void stop()
    {
        if(!running) return;
        running = false;
    }
    
    public void addLogic(ServerLogic logic)
    {
        logicSet.add(logic);
    }
    
    public void run()
    {
        while(running)
        {
            for(ServerLogic logic : logicSet)
                logic.logic();
        }
        
        for(ServerLogic logic : logicSet)
            logic.clean();
    }
}
