package net.lodoma.lime.gui;

import net.lodoma.lime.input.Input;

public abstract class UIGroupMember extends UIClickable
{
    @SuppressWarnings("rawtypes")
    public UIGroup group;
    public boolean selected;
    
    @SuppressWarnings("rawtypes")
    public UIGroupMember(UIGroup group)
    {
        this.group = group;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void onMousePress(int button, boolean state)
    {
        if (button == Input.MOUSE_BUTTON_1 && state == false)
            if (group == null)
                selected = !selected;
            else
                group.select(this);
    }
    
    public void select()
    {
        selected = true;
    }
    
    public void deselect()
    {
        selected = false;
    }
}
