package net.lodoma.lime.gui;

import java.util.ArrayList;
import java.util.List;

import net.lodoma.lime.input.Input;
import net.lodoma.lime.util.Vector2;

import org.lwjgl.opengl.GL11;

public class GUIContainer
{
    private List<GUIElement> elements;
    
    public GUIContainer()
    {
        elements = new ArrayList<GUIElement>();
    }
    
    public void addElement(GUIElement element)
    {
        elements.add(element);
    }
    
    public void removeElement(GUIElement element)
    {
        elements.remove(element);
    }
    
    public void removeAll()
    {
        elements.clear();
    }
    
    public void create()
    {
        for(GUIElement element : elements)
            element.create();
    }
    
    public void destroy()
    {
        for(GUIElement element : elements)
            element.destroy();
    }
    
    public void update(double timeDelta)
    {
        Vector2 mousePosition = Input.getMousePosition();
        
        for(GUIElement element : elements)
            element.update(timeDelta, mousePosition);
    }
    
    public void render()
    {
        GL11.glPushMatrix();
        for(GUIElement element : elements)
            element.render();
        GL11.glPopMatrix();
    }
}
