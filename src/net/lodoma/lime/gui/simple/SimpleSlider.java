package net.lodoma.lime.gui.simple;

import org.lwjgl.opengl.GL11;

import net.lodoma.lime.gui.Color;
import net.lodoma.lime.gui.Rectangle;
import net.lodoma.lime.gui.Slider;
import net.lodoma.lime.gui.SliderListener;
import net.lodoma.lime.util.TrueTypeFont;
import net.lodoma.lime.util.Vector2;

public class SimpleSlider extends Slider
{
    private static final Color SLIDER_COLOR = new Color(0.0f, 0.5f, 1.0f);
    private static final Color LINE_COLOR = new Color(0.0f, 0.5f, 1.0f);
    private static final Color OUTLINE_COLOR = new Color(1.0f, 1.0f, 1.0f);
    private static final float SLIDER_WIDTH = 0.0075f;
    private static final float SLIDER_SPEED = 6.0f;
    
    private SimpleText text;
    
    private float visualPosition;
    
    public SimpleSlider(Rectangle bounds, float value, String text, SliderListener listener)
    {
        super(new Rectangle(bounds.x + bounds.w * 0.4f, bounds.y, bounds.w * 0.6f, bounds.h), value, listener);
        this.text = new SimpleText(bounds.x, bounds.y, 0.0f, bounds.h, text, new Color(1.0f, 1.0f, 1.0f), TrueTypeFont.ALIGN_LEFT);
        this.visualPosition = getValue();
    }
    
    @Override
    public void create()
    {
        super.create();
        text.create();
    }
    
    @Override
    public void destroy()
    {
        super.destroy();
        text.destroy();
    }
    
    @Override
    public void update(double timeDelta, Vector2 mousePosition)
    {
        super.update(timeDelta, mousePosition);
        visualPosition += (getValue() - visualPosition) * timeDelta * SLIDER_SPEED;
    }
    
    @Override
    public void render()
    {
        GL11.glPushMatrix();
        
        Rectangle bounds = getBounds();
        GL11.glTranslatef(bounds.x, bounds.y, 1.0f);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        
        if(isMouseHovering())
        {
            OUTLINE_COLOR.set();
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
        
        LINE_COLOR.set();
        GL11.glBegin(GL11.GL_LINES);
        {
            GL11.glVertex2f(0.0f, bounds.h / 2);
            GL11.glVertex2f(bounds.w, bounds.h / 2);
        }
        GL11.glEnd();

        SLIDER_COLOR.set();
        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glVertex2f(visualPosition * bounds.w - SLIDER_WIDTH / 2, 0.0f);
            GL11.glVertex2f(visualPosition * bounds.w - SLIDER_WIDTH / 2, bounds.h);
            GL11.glVertex2f(visualPosition * bounds.w + SLIDER_WIDTH / 2, bounds.h);
            GL11.glVertex2f(visualPosition * bounds.w + SLIDER_WIDTH / 2, 0.0f);
        }
        GL11.glEnd();
        
        GL11.glPopMatrix();
        
        text.render();
        
    }
}
