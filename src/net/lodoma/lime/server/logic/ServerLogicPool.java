package net.lodoma.lime.server.logic;

import java.util.HashSet;
import java.util.Set;

import net.lodoma.lime.server.Server;
import net.lodoma.lime.util.Timer;

public class ServerLogicPool implements Runnable
{
    private Server server;
    private double ups;
    
    private boolean running = false;
    private Thread thread;
    
    private Set<ServerLogic> logicSet;
    
    public ServerLogicPool(Server server, double ups)
    {
        this.server = server;
        this.ups = ups;
        logicSet = new HashSet<ServerLogic>();
    }
    
    public void init()
    {
        for(ServerLogic logic : logicSet) logic.baseInit(server);
        for(ServerLogic logic : logicSet) logic.propertyInit();
        for(ServerLogic logic : logicSet) logic.fetchInit();
        for(ServerLogic logic : logicSet) logic.generalInit();
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
        Timer timer = new Timer();
        while(running)
        {
            timer.update();
            for(ServerLogic logic : logicSet)
                logic.logic();
            timer.update();
            double delta = timer.getDelta();
            double required = 1.0f / ups;
            double freetime = required - delta;
            int millis = (int) (freetime * 1000);
            int nanos = (int) (freetime * 1000000000 - millis * 1000000);
            if(nanos > 0)
                try
                {
                    Thread.sleep(millis, nanos);
                }
                catch(InterruptedException e)
                {
                    server.setCloseMessage("Logic pool failed to sleep");
                    server.closeInThread();
                }
        }
        
        for(ServerLogic logic : logicSet)
            logic.clean();
    }
}
