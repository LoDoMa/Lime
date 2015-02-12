package net.lodoma.lime.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
        }
        catch (IOException e)
        {
            server.setCloseMessage("Service failed to open");
            server.closeInThread();
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
            e.printStackTrace();
        }
    }
    
    public void start()
    {
        if(running) return;
        running = true;
        openService();
        thread = new Thread(this, "ServerServiceThread");
        thread.start();
    }
    
    public void stop()
    {
        if(!running) return;
        running = false;
        closeService();
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
                    // TODO: log exception
                    server.setCloseMessage("Service exception");
                    server.closeInThread();
                }
            }
        }
    }
}
