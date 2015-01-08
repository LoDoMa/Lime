package net.lodoma.lime.script.event;

import java.util.LinkedList;
import java.util.Queue;

import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.util.IdentityPool;

public class EventManager implements Identifiable<Integer>
{
    public int identifier;
    public IdentityPool<EventListener> listeners;
    public Queue<Object[]> events;
    
    public EventManager(int hash)
    {
        identifier = hash;
        listeners = new IdentityPool<EventListener>(false);
        events = new LinkedList<Object[]>();
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
    
    public void newEvent(Object... data)
    {
    	synchronized(events)
    	{
    		events.add(data);
    	}
    }
    
    public void runEvents()
    {
    	synchronized(events)
    	{
            listeners.foreach((EventListener listener) -> {
            	events.forEach((Object[] data) -> {
            		listener.onEvent(data);
            	});
            });
            events.clear();
    	}
    }
}
