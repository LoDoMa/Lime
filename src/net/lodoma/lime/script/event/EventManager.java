package net.lodoma.lime.script.event;

import java.util.PriorityQueue;

import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.util.IdentityPool;

public class EventManager implements Identifiable<Integer>
{
    public static class Event
    {
        public EventManager manager;
        public long eventTime;
        public Object[] data;
        
        public Event(EventManager manager, long eventTime, Object[] data)
        {
            this.manager = manager;
            this.eventTime = eventTime;
            this.data = data;
        }
    }
    
    public static PriorityQueue<Event> events = new PriorityQueue<Event>((eventA, eventB) -> {
        return eventA.eventTime < eventB.eventTime ? -1
             : eventA.eventTime > eventB.eventTime ? 1 : 0;
    });
    
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
    
    public void newEvent(Object... data)
    {
    	synchronized(events)
    	{
    		events.add(new Event(this, System.nanoTime(), data));
    	}
    }
    
    public void runEvent(Object[] data)
    {
        listeners.foreach((EventListener listener) -> {
            listener.onEvent(data);
        });
    }
    
    public static void runEvents()
    {
        synchronized(events)
        {
            Event event;
            while ((event = events.poll()) != null)
                event.manager.runEvent(event.data);
        }
    }
}
