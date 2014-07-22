package net.lodoma.lime.event;

import java.util.ArrayList;
import java.util.List;

public final class EventManager
{
    private List<EventListener> listeners;
    
    public EventManager()
    {
        listeners = new ArrayList<EventListener>();
    }
    
    public void addListener(EventListener listener)
    {
        listeners.add(listener);
    }
    
    public void removeListener(EventListener listener)
    {
        listeners.remove(listener);
    }
    
    public void onEvent(EventBundle bundle)
    {
        for(EventListener listener : listeners)
            listener.onEvent(bundle);
    }
}
