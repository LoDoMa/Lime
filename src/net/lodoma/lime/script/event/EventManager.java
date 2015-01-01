package net.lodoma.lime.script.event;

import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.util.IdentityPool;

public class EventManager implements Identifiable<Integer>
{
    public int identifier;
    public IdentityPool<EventListener> listeners;
    
    public EventManager(int hash)
    {
        identifier = hash;
        listeners = new IdentityPool<EventListener>(false);
    }
    
    @Override
    public Integer getIdentifier()
    {
        return identifier;
    }
    
    @Override
    public void setIdentifier(Integer identifier)
    {
        throw new UnsupportedOperationException();
    }
    
    public void event(Object... data)
    {
        listeners.foreach((EventListener listener) -> {
            listener.onEvent(data);
        });
    }
}