package net.lodoma.lime.server.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerUser;

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
    public void init(Server server)
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
        user.setIdentifier(idCounter++);
        users.put(user.getIdentifier(), user);
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
            
            if(user.closed)
            {
                user.stop();
                toRemove.add(user);
            }
        }
        users.remove(toRemove);
        userSet.removeAll(toRemove);
    }
    
    public void foreach(Consumer<ServerUser> consumer)
    {
        // We create a new set, so that the consumer can remove elements
        Set<ServerUser> userSet = new HashSet<ServerUser>(this.userSet);
        for (ServerUser user : userSet)
            consumer.accept(user);
    }
}
