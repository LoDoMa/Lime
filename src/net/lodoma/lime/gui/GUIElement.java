package net.lodoma.lime.gui;

import net.lodoma.lime.util.Vector2;

public interface GUIElement
{
    public void create();
    public void destroy();
    
    public void update(float timeDelta, Vector2 mousePosition);
    public void render();
}
