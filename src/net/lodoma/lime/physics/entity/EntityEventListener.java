package net.lodoma.lime.physics.entity;

import net.lodoma.lime.event.EventBundle;
import net.lodoma.lime.event.EventListener;
import net.lodoma.lime.event.EventManager;
import net.lodoma.lime.script.LuaScript;

public class EntityEventListener implements EventListener
{
    private int eventHash;
    private EventManager manager;
    private LuaScript script;
    
    public EntityEventListener(int eventHash, EventManager manager, LuaScript script)
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
