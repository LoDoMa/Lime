package net.lodoma.lime.script.event;

import net.lodoma.lime.util.Identifiable;

public abstract class EventListener implements Identifiable<Integer>
{
    public int identifier;
    
    public abstract void onEvent(Object... data);
    
    @Override
    public Integer getIdentifier()
    {
        return identifier;
    }
    
    @Override
    public void setIdentifier(Integer identifier)
    {
        this.identifier = identifier;
    }
}
