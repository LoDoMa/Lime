package net.lodoma.lime.gui.exp;

/* A lot of methods in this class have default visibility so that they don't get
   used anywhere but in the UIObject class. */
public abstract class UILayout
{
    protected UIObject object;
    
    void setObject(UIObject object)
    {
        this.object = object;
    }
    
    /* Override this, because we might be doing some checks here. */
    protected void rebuild()
    {
        // If this statement is somehow true, we have a problem.
        if (object == null)
            throw new IllegalStateException();
    }
}
