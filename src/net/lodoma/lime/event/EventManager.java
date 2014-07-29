package net.lodoma.lime.event;

import java.util.ArrayList;
import java.util.List;

import net.lodoma.lime.util.HashHelper;

public final class EventManager
{
    public static final int ON_NEW_USER_HASH = HashHelper.hash32("Lime::OnNewUser");
    
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
