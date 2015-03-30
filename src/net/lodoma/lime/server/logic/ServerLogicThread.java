package net.lodoma.lime.server.logic;

import net.lodoma.lime.Lime;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.packet.SPHInputState;
import net.lodoma.lime.server.packet.SPSnapshot;
import net.lodoma.lime.util.Timer;

public class ServerLogicThread implements Runnable
{
    public static final double SNAPSHOT_PS = 40;
    public static final double SNAPSHOT_MAXTIME = 1.0 / SNAPSHOT_PS;
    public double snapshotTime = SNAPSHOT_MAXTIME;
    
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
        commonInit();

        Timer deltaTimer = new Timer();
        Timer sleepTimer = new Timer();
        while (running)
        {
            sleepTimer.update();
            deltaTimer.update();

            double timeDelta = deltaTimer.getDelta();
            commonUpdate(timeDelta);
            
            if (server.logic != null)
            {
                if (server.logic != initialized)
                {
                    if (initialized != null)
                        initialized.destroy();
                    initialized = server.logic;
                    initialized.init();
                }

                server.logic.update(timeDelta);
            }
            
            commonPostUpdate(timeDelta);
            
            sleepTimer.update();
            
            double delta = sleepTimer.getDelta();
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
        
        commonDestroy();
    }
    
    private void commonInit()
    {
        server.spPool.add(new SPSnapshot(server));
        server.sphPool.add(new SPHInputState(server));
        
        server.userManager.init(server);
    }
    
    private void commonDestroy()
    {
        server.userManager.clean();
    }
    
    private void commonUpdate(double timeDelta)
    {
        server.userManager.update();
    }
    
    private void commonPostUpdate(double timeDelta)
    {
        snapshotTime -= timeDelta;
        if (snapshotTime <= 0.0)
            server.logic.sendSnapshots();
        
        while (snapshotTime <= 0.0)
            snapshotTime += SNAPSHOT_MAXTIME;
    }
}
