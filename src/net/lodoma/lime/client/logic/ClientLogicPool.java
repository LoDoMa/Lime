package net.lodoma.lime.client.logic;

import java.util.HashSet;
import java.util.Set;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.util.Timer;

public class ClientLogicPool implements Runnable
{
    private Client client;
    private double ups;
    
    private boolean running = false;
    private Thread thread;
    
    private Set<ClientLogic> logicSet;
    
    public ClientLogicPool(Client client, double ups)
    {
        this.client = client;
        this.ups = ups;
        
        logicSet = new HashSet<ClientLogic>();
    }
    
    public void init()
    {
        for(ClientLogic logic : logicSet)
            logic.baseInit(client);
        for(ClientLogic logic : logicSet)
            logic.propertyInit();
        for(ClientLogic logic : logicSet)
            logic.fetchInit();
        for(ClientLogic logic : logicSet)
            logic.generalInit();
    }
    
    public void start()
    {
        if(running) return;
        running = true;
        
        thread = new Thread(this, "ClientLogicThread");
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
    
    public void addLogic(ClientLogic logic)
    {
        logicSet.add(logic);
    }
    
    public void run()
    {
        Timer timer = new Timer();
        while(running)
        {
            timer.update();
            for(ClientLogic logic : logicSet)
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
                    e.printStackTrace();
                }
        }
        
        for(ClientLogic logic : logicSet)
            logic.clean();
    }
}
