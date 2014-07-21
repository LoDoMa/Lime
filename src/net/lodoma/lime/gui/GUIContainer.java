package net.lodoma.lime.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class GUIContainer
{
    private float scaleX;
    private float scaleY;
    
    private List<GUIElement> elements;
    
    public GUIContainer(float scaleX, float scaleY)
    {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        
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
    
    public void update(float timeDelta)
    {
        for(GUIElement element : elements)
            element.update(timeDelta);
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
