package net.lodoma.lime.gui.exp;

public class UIGroup<T extends UIGroupMember>
{
    public UICallback onChange;
    public T selected;
    
    public UIGroup(UICallback onChange)
    {
        this.onChange = onChange;
    }
    
    public void select(T newSelected)
    {
        if (selected == newSelected)
            return;
        if (selected != null)
            selected.deselect();
        selected = newSelected;
        selected.select();
        if (onChange != null)
            onChange.call();
    }
}
