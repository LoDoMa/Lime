package net.joritan.jlime.gui;

public abstract class GUIElement
{
    protected final GUIEnvironment environment;
    
    public GUIElement(GUIEnvironment environment)
    {
        this.environment = environment;
    }
    
    public abstract void update(float timeDelta);
    public abstract void render();
}
