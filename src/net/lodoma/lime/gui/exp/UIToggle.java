package net.lodoma.lime.gui.exp;

import net.lodoma.lime.input.Input;

public class UIToggle extends UIClickable implements UIGroupMember
{
    public UIGroup group;
    public boolean selected;
    
    public UIToggle(UIGroup group)
    {
        this.group = group;
    }
    
    @Override
    public void onMousePress(int button, boolean state)
    {
        if (button == Input.MOUSE_BUTTON_1 && state == false)
        {
            if (group == null)
                selected = !selected;
            else
                group.select(this);
        }
    }
    
    @Override
    public void select()
    {
        selected = true;
    }
    
    @Override
    public void deselect()
    {
        selected = false;
    }
}
