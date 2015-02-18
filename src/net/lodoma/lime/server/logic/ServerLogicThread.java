package net.lodoma.lime.server.logic;

import net.lodoma.lime.Lime;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.util.Timer;

public class ServerLogicThread implements Runnable
{
    private Server server;
    private boolean running = false;
    private Thread thread;
    
    private int ups;
    
    private ServerLogic initialized;
    
    public ServerLogicThread(Server server, int ups)
    {
        this.server = server;
        this.ups = ups;
    }
    
    public void start()
    {
        if (running) return;
        running = true;
        
        thread = new Thread(this, "Logic");
        thread.start();
    }
    
    public void stop()
    {
        if (!running) return;
        running = false;
    }
    
    public boolean isRunning()
    {
        return thread.isAlive();
    }
    
    @Override
    public void run()
    {
        server.userManager.init(server);
        
        Timer timer = new Timer();
        while (running)
        {
            timer.update();
            
            if (server.logic != null)
            {
                if (server.logic != initialized)
                {
                    if (initialized != null)
                        initialized.destroy();
                    initialized = server.logic;
                    initialized.init();
                }
                
                commonUpdate();
                server.logic.update();
            }
            
            timer.update();
            
            double delta = timer.getDelta();
            double required = 1.0f / ups;
            double freetime = required - delta;
            int millis = (int) (freetime * 1000);
            int nanos = (int) (freetime * 1000000000 - millis * 1000000);
            
            if (millis > 0 || nanos > 0)
                try
                {
                    Thread.sleep(millis, nanos);
                }
                catch (InterruptedException e)
                {
                    Lime.LOGGER.C("Unexpected interrupt in logic thread");
                    Lime.LOGGER.log(e);
                    Lime.forceExit(e);
                }
        }
        
        if (initialized != null)
            initialized.destroy();
        
        server.userManager.clean();
    }
    
    private void commonUpdate()
    {
        server.userManager.update();
    }
}
