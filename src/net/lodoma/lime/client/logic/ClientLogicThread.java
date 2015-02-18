package net.lodoma.lime.client.logic;

import java.io.DataInputStream;
import java.io.IOException;

import net.lodoma.lime.Lime;
import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacketHandler;
import net.lodoma.lime.client.packet.CPHSnapshot;
import net.lodoma.lime.client.packet.CPInputState;
import net.lodoma.lime.util.Timer;

public class ClientLogicThread implements Runnable
{
    private Client client;
    private boolean running = false;
    private Thread thread;
    
    private int ups;
    
    public ClientLogicThread(Client client, int ups)
    {
        this.client = client;
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
        
        Timer timer = new Timer();
        while (running)
        {
            timer.update();

            commonUpdate();
            
            if (client.logic != null)
                client.logic.update();
            
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
        
        if (client.logic != null)
            client.logic.destroy();
        
        commonDestroy();
    }
    
    private void commonInit()
    {
        client.cpPool.add(new CPInputState(client));
        client.cphPool.add(new CPHSnapshot(client));
    }
    
    private void commonDestroy()
    {
        
    }
    
    private void commonUpdate()
    {
        try
        {
            DataInputStream stream = client.getInputStream();
            
            int amountHandled = 0;
            while (stream.available() >= 4 && amountHandled < 1)
            {
                int hash = stream.readInt();
                ClientPacketHandler cih = client.cphPool.get(hash);
                if (cih != null)
                    cih.handle();
                
                amountHandled++;
            }
        }
        catch (IOException e)
        {
            Lime.LOGGER.C("IO exception in CPH handling");
            Lime.LOGGER.log(e);
            Lime.forceExit(e);
        }
    }
}
