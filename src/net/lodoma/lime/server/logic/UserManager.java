package net.lodoma.lime.server.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.util.SystemHelper;

public class UserManager implements ServerLogic
{
    private int idCounter;
    private Set<ServerUser> userSet;
    private Map<Integer, ServerUser> users;
    
    public UserManager()
    {
        idCounter = 0;
        userSet = Collections.synchronizedSet(new HashSet<ServerUser>());
        users = Collections.synchronizedMap(new HashMap<Integer, ServerUser>());
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
        for(ServerUser user : userSet)
            user.stop();
        userSet.clear();
        users.clear();
    }
    
    public boolean addUser(ServerUser user)
    {
        user.setID(idCounter++);
        users.put(user.getID(), user);
        userSet.add(user);
        return true;
    }
    
    public ServerUser getUser(int id)
    {
        return users.get(id);
    }
    
    public Set<ServerUser> getUserSet()
    {
        return userSet;
    }
    
    @Override
    public void logic()
    {
        long currentTime = SystemHelper.getTimeNanos();
        
        List<ServerUser> toRemove = new ArrayList<ServerUser>();
        for(ServerUser user : userSet)
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
            
            users.remove(toRemove);
        }
        userSet.removeAll(toRemove);
        
    }
}
