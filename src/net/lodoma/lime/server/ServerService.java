package net.lodoma.lime.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import net.lodoma.lime.common.NetStage;
import net.lodoma.lime.server.logic.UserManager;

public class ServerService implements Runnable
{
    private Thread thread;
    private boolean running;
    
    private int port;
    private ServerSocket serviceSocket;
    
    private Server server;
    private UserManager userManager;
    
    public ServerService(Server server)
    {
        this.server = server;
        userManager = (UserManager) server.getProperty("userManager");
    }
    
    public void setPort(int port)
    {
        this.port = port;
    }
    
    private void openService()
    {
        try
        {
            serviceSocket = new ServerSocket(port);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private void closeService()
    {
        try
        {
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
    
    @Override
    public void run()
    {
        while(running)
        {
            try
            {
                Socket clientSocket = serviceSocket.accept();
                ServerUser serverUser = new ServerUser(NetStage.DEPENDENCY, clientSocket, server);
                serverUser.start();
                userManager.addUser(serverUser);
            }
            catch(IOException e)
            {
                if(running)
                    e.printStackTrace();
            }
        }
    }
}
