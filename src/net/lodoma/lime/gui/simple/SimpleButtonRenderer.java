package net.lodoma.lime.gui.simple;

import net.lodoma.lime.gui.Button;
import net.lodoma.lime.gui.ButtonRenderer;
import net.lodoma.lime.gui.Color;
import net.lodoma.lime.gui.Rectangle;
import net.lodoma.lime.gui.Text;
import static org.lwjgl.opengl.GL11.*;

public class SimpleButtonRenderer implements ButtonRenderer
{
    private static final Color BODY_COLOR = new Color(0.2f, 0.2f, 0.2f, 0.2f);
    private static final Color OUTLINE_COLOR = new Color(1.0f, 1.0f, 1.0f);

    private Text text;
    private float transparency;
    
    public void setText(Text text)
    {
        this.text = text;
    }
    
    public void setTransparency(float transparency)
    {
        this.transparency = transparency;
    }
    
    @Override
    public void render(Button button)
    {
        if(transparency != 0.0f)
        {
            Rectangle bounds = button.getBounds();
            
            glBindTexture(GL_TEXTURE_2D, 0);
            
            glBegin(GL_LINES);
            {
                OUTLINE_COLOR.set();
                glVertex2f(0.0f, 0.0f);
                glVertex2f(bounds.w, 0.0f);
                glVertex2f(bounds.w, 0.0f);
                glVertex2f(bounds.w, bounds.h);
                glVertex2f(bounds.w, bounds.h);
                glVertex2f(0.0f, bounds.h);
                glVertex2f(0.0f, bounds.h);
                glVertex2f(0.0f, 0.0f);
            }
            glEnd();
            
            glBegin(GL_QUADS);
            {
                BODY_COLOR.set(transparency);
                glVertex2f(0.0f, 0.0f);
                glVertex2f(bounds.w, 0.0f);
                glVertex2f(bounds.w, bounds.h);
                glVertex2f(0.0f, bounds.h);
            }
            glEnd();
        }
        
        if(text != null)
            text.render();
    }
}
