package net.lodoma.lime.client.logic;

import java.util.HashSet;
import java.util.Set;

import net.lodoma.lime.client.Client;

public class ClientLogicPool implements Runnable
{
    private Client client;
    
    private boolean running = false;
    private Thread thread;
    
    private Set<ClientLogic> logicSet;
    
    public ClientLogicPool(Client client)
    {
        this.client = client;
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
        
        thread = new Thread(this);
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
        while(running)
        {
            for(ClientLogic logic : logicSet)
                logic.logic();
        }
        
        for(ClientLogic logic : logicSet)
            logic.clean();
    }
}
