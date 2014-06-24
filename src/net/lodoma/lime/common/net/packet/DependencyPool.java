package net.lodoma.lime.common.net.packet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.lodoma.lime.server.generic.ServerUser;
import net.lodoma.lime.server.generic.net.packet.ServerPacket;

public class DependencyPool
{
    private Map<ServerUser, Integer> userProgress;
    private List<ServerPacket> dependencies;
    
    public DependencyPool()
    {
        userProgress = new HashMap<ServerUser, Integer>();
        dependencies = new ArrayList<ServerPacket>();
    }
    
    public void addDependency(ServerPacket packet)
    {
        dependencies.add(packet);
    }
    
    public boolean hasNextDependency(ServerUser user)
    {
        if(!userProgress.containsKey(user))
            userProgress.put(user, 0);
        int progress = userProgress.get(user);
        return progress != dependencies.size();
    }
    
    public ServerPacket nextDependency(ServerUser user)
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