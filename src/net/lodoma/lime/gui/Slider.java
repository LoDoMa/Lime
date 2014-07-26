package net.lodoma.lime.gui;

import org.lwjgl.opengl.GL11;

import net.lodoma.lime.input.Input;
import net.lodoma.lime.util.Vector2;

public class Slider implements GUIElement
{
    private Rectangle bounds;
    private float sliderWidth;
    private float position;
    private float visualPosition;
    private Color sliderColor;
    private Color lineColor;
    private Color outlineColor;
    
    private boolean hover;
    
    public Slider(Rectangle bounds, float sliderWidth, float initpos, Color sliderColor, Color lineColor, Color outlineColor)
    { 
        this.bounds = bounds;
        this.sliderWidth = sliderWidth;
        this.position = initpos;
        this.visualPosition = initpos;
        this.sliderColor = sliderColor;
        this.lineColor = lineColor;
        this.outlineColor = outlineColor;
    }
    
    public float getValue()
    {
        return position;
    }
    
    @Override
    public void create()
    {
        
    }
    
    @Override
    public void destroy()
    {
        
    }
    
    @Override
    public void update(double timeDelta, Vector2 mousePosition)
    {
        if(bounds.inside(mousePosition.x, mousePosition.y))
        {
            hover = true;
            if(Input.getMouse(Input.LEFT_MOUSE_BUTTON))
                position = (mousePosition.x - bounds.x) / bounds.w;
        }
        else
            hover = false;
        
        visualPosition += (position - visualPosition) * timeDelta * 6.0f;
    }
    
    @Override
    public void render()
    {
        GL11.glPushMatrix();
        
        GL11.glTranslatef(bounds.x, bounds.y, 1.0f);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        
        if(hover)
        {
            outlineColor.set();
            GL11.glBegin(GL11.GL_LINES);
            {
                GL11.glVertex2f(0.0f, 0.0f);
                GL11.glVertex2f(bounds.w, 0.0f);
                GL11.glVertex2f(bounds.w, 0.0f);
                GL11.glVertex2f(bounds.w, bounds.h);
                GL11.glVertex2f(bounds.w, bounds.h);
                GL11.glVertex2f(0.0f, bounds.h);
                GL11.glVertex2f(0.0f, bounds.h);
                GL11.glVertex2f(0.0f, 0.0f);
            }
            GL11.glEnd();
        }
        
        lineColor.set();
        GL11.glBegin(GL11.GL_LINES);
        {
            GL11.glVertex2f(0.0f, bounds.h / 2);
            GL11.glVertex2f(bounds.w, bounds.h / 2);
        }
        GL11.glEnd();

        sliderColor.set();
        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glVertex2f(visualPosition * bounds.w - sliderWidth / 2, 0.0f);
            GL11.glVertex2f(visualPosition * bounds.w - sliderWidth / 2, bounds.h);
            GL11.glVertex2f(visualPosition * bounds.w + sliderWidth / 2, bounds.h);
            GL11.glVertex2f(visualPosition * bounds.w + sliderWidth / 2, 0.0f);
        }
        GL11.glEnd();
        
        GL11.glPopMatrix();
    }
}
