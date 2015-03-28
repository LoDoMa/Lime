package net.lodoma.lime.rui;

public class RUIGroup
{
    private RUIActivable active;
    
    public RUIActivable getActive()
    {
        return active;
    }
    
    public void setActive(RUIActivable element)
    {
        if (active == element)
            return;
        
        RUIActivable prevActive = active;
        active = element;
        if (prevActive != null)
            prevActive.setActivated(false);
    }
}
