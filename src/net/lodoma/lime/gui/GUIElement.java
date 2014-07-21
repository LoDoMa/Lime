package net.lodoma.lime.gui;

public interface GUIElement
{
    public void create();
    public void destroy();
    
    public void update(float timeDelta);
    public void render();
}
