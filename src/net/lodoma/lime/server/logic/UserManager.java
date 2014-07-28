package net.lodoma.lime.server.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.util.SystemHelper;

public class UserManager implements ServerLogic
{
    private List<ServerUser> users;
    
    public UserManager()
    {
        users = Collections.synchronizedList(new ArrayList<ServerUser>());
    }
    
    @Override
    public void baseInit(Server server)
    {
        
    }
    
    @Override
    public void propertyInit()
    {
        
    }
    
    @Override
    public void fetchInit()
    {
        
    }
    
    @Override
    public void generalInit()
    {
        
    }
    
    @Override
    public void clean()
    {
        for(ServerUser user : users)
            user.stop();
        users.clear();
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
    public void logic()
    {
        long currentTime = SystemHelper.getTimeNanos();
        
        List<ServerUser> toRemove = new ArrayList<ServerUser>();
        for(ServerUser user : users)
        {
            try
            {
                user.handleInput();
            }
            catch (IOException e)
            {
                user.stop();
                toRemove.add(user);
                continue;
            }
            
            long userLastTime = user.getLastResponseTime();
            long timeDiff = currentTime - userLastTime;
            
            if(timeDiff > 5000000000L)
            {
                user.stop();
                toRemove.add(user);
            }
        }
        users.removeAll(toRemove);
    }
}
