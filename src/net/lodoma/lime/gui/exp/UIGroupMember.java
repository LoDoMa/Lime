package net.lodoma.lime.gui.exp;

import net.lodoma.lime.input.Input;

public abstract class UIGroupMember extends UIClickable
{
    public UIGroup group;
    public boolean selected;
    
    public UIGroupMember(UIGroup group)
    {
        this.group = group;
    }
    
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
