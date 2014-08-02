package net.lodoma.lime.script;

import net.lodoma.lime.event.EventBundle;
import net.lodoma.lime.event.EventListener;
import net.lodoma.lime.event.EventManager;

public class LuaEventListener implements EventListener
{
    private int eventHash;
    private EventManager manager;
    private LuaScript script;
    
    public LuaEventListener(int eventHash, EventManager manager, LuaScript script)
    {
        this.eventHash = eventHash;
        this.manager = manager;
        this.script = script;
        
        manager.addListener(this);
    }
    
    @Override
    public void onEvent(EventBundle bundle)
    {
        script.call("lime.listener.invoke", eventHash, bundle);
    }
    
    public void destroy()
    {
        manager.removeListener(this);
    }
}
