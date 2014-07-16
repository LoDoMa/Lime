package net.lodoma.lime.server;

import java.util.ArrayList;
import java.util.List;

import net.lodoma.lime.util.SystemHelper;

public class UserManager implements Runnable
{
    private Thread thread;
    private boolean running;
    
    private List<ServerUser> users;
    
    public UserManager()
    {
        users = new ArrayList<ServerUser>();
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
        running = false;
    }
    
    public void addUser(ServerUser user)
    {
        users.add(user);
    }
    
    public List<ServerUser> getUserList()
    {
        return new ArrayList<ServerUser>(users);
    }
    
    @Override
    public void run()
    {
        while(running)
        {
            long currentTime = SystemHelper.getTimeNanos();
            
            List<ServerUser> removedUsers = new ArrayList<ServerUser>();
            for(ServerUser user : users)
            {
                long userLastTime = user.getLastResponseTime();
                long timeDiff = currentTime - userLastTime;
                
                if(timeDiff > 5000)
                {
                    System.out.println("UserManager.java:48 - user removed - no response");
                    user.stop();
                    removedUsers.add(user);
                }
            }
            users.removeAll(removedUsers);
            
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
