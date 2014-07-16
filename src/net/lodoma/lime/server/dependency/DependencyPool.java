package net.lodoma.lime.server.dependency;

import java.util.ArrayList;
import java.util.List;

import net.lodoma.lime.server.ServerOutput;

public class DependencyPool
{
    private List<ServerOutput> dependencies;
    
    public DependencyPool()
    {
        dependencies = new ArrayList<ServerOutput>();
    }
    
    public void addDependency(ServerOutput dependency)
    {
        dependencies.add(dependency);
    }
    
    public ServerOutput getDependency(int index)
    {
        if(dependencies.size() <= index)
            return null;
        return dependencies.get(index);
    }
}
