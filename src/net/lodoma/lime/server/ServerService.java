package net.lodoma.lime.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import net.lodoma.lime.Lime;

public class ServerService implements Runnable
{
    private Thread thread;
    private boolean running;
    
    private ServerSocket serviceSocket;
    
    private Server server;
    
    public ServerService(Server server)
    {
        this.server = server;
    }
    
    private void openService()
    {
        try
        {
            serviceSocket = new ServerSocket(NetSettings.PORT);
            Lime.LOGGER.F("Opened socket; port = " + NetSettings.PORT + "");
        }
        catch (IOException e)
        {
            Lime.LOGGER.C("Failed to open server service");
            Lime.LOGGER.log(e);
            Lime.forceExit();
        }
    }
    
    private void closeService()
    {
        try
        {
            if(serviceSocket != null && !serviceSocket.isClosed())
                serviceSocket.close();
        }
        catch (IOException e)
        {
            Lime.LOGGER.C("Failed to close server service");
            Lime.LOGGER.log(e);
            Lime.forceExit();
        }
    }
    
    public void start()
    {
        if(running) return;
        running = true;
        openService();
        thread = new Thread(this, "ServerServiceThread");
        thread.start();
        Lime.LOGGER.F("Server service started");
    }
    
    public void stop()
    {
        if(!running) return;
        running = false;
        closeService();
        Lime.LOGGER.F("Server service closed");
    }
    
    public boolean isRunning()
    {
        return thread.isAlive();
    }
    
    @Override
    public void run()
    {
        while(running)
        {
            if(serviceSocket == null) break;
            try
            {
                Socket clientSocket = serviceSocket.accept();
                ServerUser serverUser = new ServerUser(clientSocket, server);
                if (server.userManager.addUser(serverUser))
                    serverUser.start();
            }
            catch(IOException e)
            {
                if(running)
                {
                    Lime.LOGGER.C("Unexpected exception in server service");
                    Lime.LOGGER.log(e);
                    Lime.forceExit();
                }
            }
        }
    }
}
