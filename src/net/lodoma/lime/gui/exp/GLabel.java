package net.lodoma.lime.gui.exp;

import net.lodoma.lime.util.TrueTypeFont;
import net.lodoma.lime.util.Vector2;

import static org.lwjgl.opengl.GL11.*;

public class GLabel extends GComponent
{
    private static final Vector2 DEFAULT_SCALE = new Vector2(1.0f, 1.0f);
    
    private TrueTypeFont ttfont = null;
    private String text = "";
    private final Vector2 scale = DEFAULT_SCALE.clone();
    private int format = TrueTypeFont.ALIGN_LEFT;
    
    public String getText()
    {
        return text;
    }
    
    public void setText(String text)
    {
        if(text == null)
            throw new NullPointerException();
        this.text = text;
    }
    
    public TrueTypeFont getFont()
    {
        return ttfont;
    }
    
    public void setFont(TrueTypeFont ttfont)
    {
        if(ttfont == null)
            throw new NullPointerException();
        this.ttfont = ttfont;
    }
    
    public Vector2 getScale()
    {
        return scale;
    }
    
    public int getFormat()
    {
        return format;
    }

    public void setFormat(int format)
    {
        this.format = format;
    }

    @Override
    public void render()
    {
        super.render();
        
        glPushMatrix();
        {
            Vector2 translation = getTranslation();
            glTranslatef(translation.x, translation.y, 0.0f);
            glRotatef(getRotation(), 0.0f, 0.0f, 1.0f);
            
            if(ttfont == null) throw new NullPointerException("font not set");
            ttfont.drawString(0.0f, 0.0f, text, scale.x, scale.y, format);
        }
        glPopMatrix();
    }
}
