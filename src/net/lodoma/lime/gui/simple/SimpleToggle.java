package net.lodoma.lime.gui.simple;

import net.lodoma.lime.gui.Button;
import net.lodoma.lime.gui.ButtonRenderer;
import net.lodoma.lime.gui.Color;
import net.lodoma.lime.gui.Rectangle;
import net.lodoma.lime.gui.Text;
import net.lodoma.lime.gui.Toggle;
import net.lodoma.lime.gui.ToggleListener;
import net.lodoma.lime.util.TrueTypeFont;
import net.lodoma.lime.util.Vector2;
import static org.lwjgl.opengl.GL11.*;

public class SimpleToggle extends Toggle implements ToggleListener
{
    private static final Color HIGHLIGHT_COLOR = new Color(0.3f, 0.3f, 0.3f, 0.3f);
    private static final Color HIGHLIGHT_OUTLINE_COLOR = new Color(0.2f, 0.2f, 0.2f, 1.0f);
    private static final Color OUTLINE_COLOR = new Color(1.0f, 1.0f, 1.0f);
    
    private class ToggleButtonRenderer implements ButtonRenderer
    {
        private Text text;
        
        public ToggleButtonRenderer(Text text)
        {
            this.text = text;
        }
        
        @Override
        public void render(Button button)
        {
            if(button.isMouseHovering())
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
            }
            text.render();
        }
    }
    
    private SimpleText buttonTrue;
    private SimpleText buttonFalse;
    
    private ToggleListener listener;
    
    private float targetX;
    private float visualX;
    private float visualY;
    private float visualW;
    private float visualH;
    
    public SimpleToggle(Rectangle bounds, boolean current, String text, String buttonTrueText, String buttonFalseText, ToggleListener listener)
    {
        super(new SimpleText(bounds.x, bounds.y, 0.0f, bounds.h, text, new Color(1.0f, 1.0f, 1.0f), TrueTypeFont.ALIGN_LEFT), current,
                new Button(new Rectangle(bounds.x + bounds.w * 0.6f, bounds.y, bounds.w * 0.2f, bounds.h), null, null),
                new Button(new Rectangle(bounds.x + bounds.w * 0.8f, bounds.y, bounds.w * 0.2f, bounds.h), null, null), null);
        setListener(this);
        
        this.buttonTrue = new SimpleText(0.0f, 0.0f, bounds.w * 0.2f, bounds.h, buttonTrueText);
        this.buttonFalse = new SimpleText(0.0f, 0.0f, bounds.w * 0.2f, bounds.h, buttonFalseText);
        
        getButton(true).setRenderer(new ToggleButtonRenderer(buttonTrue));
        getButton(false).setRenderer(new ToggleButtonRenderer(buttonFalse));
        
        this.listener = listener;
        
        targetX = getButton(current).getBounds().x;
        visualX = targetX;
        visualY = bounds.y;
        visualW = bounds.w * 0.2f;
        visualH = bounds.h;
    }
    
    @Override
    public void create()
    {
        super.create();
        buttonTrue.create();
        buttonFalse.create();
    }
    
    @Override
    public void destroy()
    {
        super.destroy();
        buttonTrue.destroy();
        buttonFalse.destroy();
    }
    
    @Override
    public void update(double timeDelta, Vector2 mousePosition)
    {
        super.update(timeDelta, mousePosition);

        visualX += (targetX - visualX) * timeDelta * 6.0f;
    }
    
    @Override
    public void onToggle(Toggle toggle, boolean newState)
    {
        targetX = getButton(newState).getBounds().x;
        if(listener != null)
            listener.onToggle(toggle, newState);
    }
    
    @Override
    public void render()
    {
        glPushMatrix();
        glTranslatef(visualX, visualY, 0.0f);

        glBindTexture(GL_TEXTURE_2D, 0);
        
        HIGHLIGHT_COLOR.set();
        glBegin(GL_QUADS);
        {
            glVertex2f(0.0f, 0.0f);
            glVertex2f(visualW, 0.0f);
            glVertex2f(visualW, visualH);
            glVertex2f(0.0f, visualH);
        }
        glEnd();

        HIGHLIGHT_OUTLINE_COLOR.set();
        glBegin(GL_LINES);
        {
            glVertex2f(0.0f, 0.0f);
            glVertex2f(visualW, 0.0f);
            glVertex2f(visualW, 0.0f);
            glVertex2f(visualW, visualH);
            glVertex2f(visualW, visualH);
            glVertex2f(0.0f, visualH);
            glVertex2f(0.0f, visualH);
            glVertex2f(0.0f, 0.0f);
        }
        glEnd();
        
        glPopMatrix();
        
        super.render();
    }
}
