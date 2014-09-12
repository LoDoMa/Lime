package net.lodoma.lime.client.logic;

import java.util.HashSet;
import java.util.Set;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.util.Timer;

/**
 * ClientLogicPool is a thread that manages ClientLogic objects.
 * First it initializes all ClientLogic objects.
 * Then it repeats the logic cycle.
 * A logic cycle calls the "logic" method in all ClientLogic objects.
 * The cycles are repeated a set amount of times in a second.
 * This amount is called UPS. (Updates Per Second)
 * 
 * @author Lovro Kalinovčić
 */
public class ClientLogicPool implements Runnable
{
    private Client client;
    private double ups;
    
    private boolean running = false;
    private Thread thread;
    
    private Set<ClientLogic> logicSet;
    
    /**
     * 
     * @param client - the client that uses this logic pool
     * @param ups - how many times a logic cycle is repeated in a second
     */
    public ClientLogicPool(Client client, double ups)
    {
        this.client = client;
        this.ups = ups;
        
        logicSet = new HashSet<ClientLogic>();
    }
    
    /**
     * Calls all four stages of initialization
     * for all added ClientLogic objects.
     */
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
    
    /**
     * Starts the thread if not running.
     * The created thread is called "ClientLogicThread"
     */
    public void start()
    {
        if(running) return;
        running = true;
        
        thread = new Thread(this, "ClientLogicThread");
        thread.start();
    }
    
    /**
     * Stops the thread if running.
     */
    public void stop()
    {
        if(!running) return;
        running = false;
    }
    
    /**
     * Tests if the thread is running.
     * @return is the thread running.
     */
    public boolean isRunning()
    {
        return thread.isAlive();
    }
    
    /**
     * Adds a ClientLogic objects to the pool.
     * @param logic - a ClientLogic object to add
     */
    public void addLogic(ClientLogic logic)
    {
        logicSet.add(logic);
    }
    
    @Override
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
                    client.setCloseMessage("Logic pool failed to sleep");
                    client.closeInThread();
                }
        }
        
        for(ClientLogic logic : logicSet)
            logic.clean();
    }
}
