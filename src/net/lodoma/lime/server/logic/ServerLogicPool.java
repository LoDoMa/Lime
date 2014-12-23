package net.lodoma.lime.server.logic;

import java.util.HashSet;
import java.util.Set;

import net.lodoma.lime.server.Server;

public class ServerLogicPool implements Runnable
{
    private Server server;
    
    private boolean running = false;
    private Thread thread;
    
    private Set<ServerLogic> logicSet;
    
    public ServerLogicPool(Server server)
    {
        this.server = server;
        logicSet = new HashSet<ServerLogic>();
    }
    
    public void init()
    {
        for(ServerLogic logic : logicSet)
            logic.init(server);
    }
    
    public void start()
    {
        if(running) return;
        running = true;
        
        thread = new Thread(this, "ServerLogicThread");
        thread.start();
    }
    
    public void stop()
    {
        if(!running) return;
        running = false;
    }
    
    public boolean isRunning()
    {
        return thread.isAlive();
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
            
            try
            {
                Thread.sleep(1);
            }
            catch(InterruptedException e)
            {
                // TODO: handle this better
                e.printStackTrace();
            }
        }
        
        for(ServerLogic logic : logicSet)
            logic.clean();
    }
}
