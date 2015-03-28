package net.lodoma.lime.rui;

import net.lodoma.lime.input.Input;

public class RUIToggle extends RUIButton implements RUIActivable
{
    protected boolean active;
    
    public RUIToggle(RUIElement parent)
    {
        super(parent);
    }
    
    @Override
    public boolean isActivated()
    {
        return active;
    }
    
    @Override
    public void setActivated(boolean flag)
    {
        if (!flag && group != null)
            if (group.getActive() == this)
                return;
        
        boolean prevActive = active;
        
        active = flag;
        if (active && group != null)
            group.setActive(this);
        
        if (prevActive != active && eventListener != null)
            eventListener.onEvent(active ? RUIEventType.ACTIVE : RUIEventType.INACTIVE, null);
    }
    
    @Override
    protected void checkClick()
    {
        super.checkClick();
        
        if (Input.getMouseDown(Input.MOUSE_BUTTON_LEFT))
            setActivated(!active);
    }
    
    @Override
    public void update(double timeDelta)
    {
        synchronized (treeLock)
        {
            super.update(timeDelta);
            
            if (active)
                if (hover)
                    state = "active:hover";
                else
                    state = "active";
        }
    }
}
