package net.lodoma.lime.gui.exp;

public class UIGroup
{
    public UICallback onChange;
    public UIGroupMember selected;
    
    public UIGroup(UICallback onChange)
    {
        this.onChange = onChange;
    }
    
    public void select(UIGroupMember newSelected)
    {
        if (selected == newSelected)
            return;
        if (selected != null)
            selected.deselect();
        selected = newSelected;
        selected.select();
        onChange.call();
    }
}
