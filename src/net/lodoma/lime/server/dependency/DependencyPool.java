package net.lodoma.lime.server.dependency;

import java.util.ArrayList;
import java.util.List;

import net.lodoma.lime.server.ServerPacket;

public class DependencyPool
{
    private List<ServerPacket> dependencies;
    
    public DependencyPool()
    {
        dependencies = new ArrayList<ServerPacket>();
    }
    
    public void addDependency(ServerPacket dependency)
    {
        dependencies.add(dependency);
    }
    
    public ServerPacket getDependency(int index)
    {
        if(dependencies.size() <= index)
            return null;
        return dependencies.get(index);
    }
}
