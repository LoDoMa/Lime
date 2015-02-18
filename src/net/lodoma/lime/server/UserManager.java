package net.lodoma.lime.server;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import net.lodoma.lime.Lime;
import net.lodoma.lime.script.event.EMOnJoin;
import net.lodoma.lime.script.event.EMOnLeave;
import net.lodoma.lime.util.IdentityPool;

public class UserManager
{
    private Server server;
    
    private IdentityPool<ServerUser> userPool;
    
    public UserManager()
    {
        userPool = new IdentityPool<ServerUser>(false);
    }
    
    public void init(Server server)
    {
        this.server = server;

        server.emanPool.add(new EMOnJoin());
        server.emanPool.add(new EMOnLeave());
    }
    
    public void clean()
    {
        userPool.foreach((ServerUser user) -> user.stop());
        userPool.clear();
    }
    
    public boolean addUser(ServerUser user)
    {
        if (server.logic.acceptUser(user))
        {
            userPool.add(user);
            
            server.emanPool.get(EMOnJoin.HASH).newEvent(user.getIdentifier());
            return true;
        }
        return false;
    }
    
    public ServerUser getUser(int id)
    {
        return userPool.get(id);
    }
    
    public int getUserCount()
    {
        return userPool.size();
    }
    
    public List<ServerUser> getUserSet()
    {
        return userPool.getObjectList();
    }
    
    public void update()
    {
        userPool.foreach((ServerUser user) -> {
            try
            {
                user.handleInput();
            }
            catch (IOException e)
            {
                user.stop();
                userPool.remove(user);
                
                Lime.LOGGER.W("Removed user " + user.identifier + " from user pool; failed to handle input");
                server.emanPool.get(EMOnLeave.HASH).newEvent(user.identifier);
                return;
            }
            
            if(user.closed)
            {
                user.stop();
                userPool.remove(user);
                
                Lime.LOGGER.I("Removed user " + user.identifier + " from user pool; closed");
                server.emanPool.get(EMOnLeave.HASH).newEvent(user.identifier);
            }
        });
    }
    
    public void foreach(Consumer<ServerUser> consumer)
    {
        userPool.foreach(consumer);
    }
}
