package net.lodoma.lime.gui;

import java.util.ArrayList;
import java.util.List;

import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.util.Vector2;

import org.lwjgl.opengl.GL11;

public class GUIContainer
{
    private float scaleX;
    private float scaleY;
    
    private List<GUIElement> elements;
    
    public GUIContainer()
    {
        this.scaleX = Window.getOrthoWidth();
        this.scaleY = Window.getOrthoHeight();
        
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
        GL11.glScalef(scaleX, scaleY, 1.0f);
        
        for(GUIElement element : elements)
            element.render();
        
        GL11.glPopMatrix();
    }
}
