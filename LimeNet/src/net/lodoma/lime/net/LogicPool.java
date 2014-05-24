package net.lodoma.lime.net;

import java.util.HashSet;
import java.util.Set;

public final class LogicPool
{
    private Set<Logic> components;
    
    public LogicPool()
    {
        components = new HashSet<Logic>();
    }
    
    public void addLogicComponent(Logic logic)
    {
        components.add(logic);
    }
    
    public Set<Logic> getLogicComponents()
    {
        return components;
    }
}
