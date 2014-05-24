package net.lodoma.lime.net.packet.dependency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.lodoma.lime.net.server.generic.ServerUser;

public class DependencyPool
{
    private Map<ServerUser, Integer> userProgress;
    private List<Long> dependencies;
    
    public DependencyPool()
    {
        userProgress = new HashMap<ServerUser, Integer>();
        dependencies = new ArrayList<Long>();
    }
    
    public void addDependency(long packetID)
    {
        dependencies.add(packetID);
    }
    
    public boolean hasNextDependency(ServerUser user)
    {
        if(!userProgress.containsKey(user))
            userProgress.put(user, 0);
        int progress = userProgress.get(user);
        return progress != dependencies.size();
    }
    
    public long nextDependency(ServerUser user)
    {
        if(!userProgress.containsKey(user))
            userProgress.put(user, 0);
        int progress = userProgress.get(user);
        userProgress.put(user, progress + 1);
        return dependencies.get(progress);
    }
    
    public void endUserProgress(ServerUser user)
    {
        if(userProgress.containsKey(user))
            userProgress.remove(user);
    }
}
