package net.lodoma.lime.gui;

import java.awt.Font;

import net.lodoma.lime.util.TrueTypeFont;
import net.lodoma.lime.util.Vector2;

public class Text implements GUIElement
{
    private static final int SIZE = 42;
    
    private float x;
    private float y;
    private float sx;
    private float sy;
    
    private String text;
    private Color textColor;
    private TrueTypeFont font;
    private int alignment;
    
    public Text(float x, float y, float sx, float sy, String text, Color textColor, String fontName, int style, int alignment)
    {
        this.x = x;
        this.y = y;
        this.sx = sx / (float) SIZE;
        this.sy = sy / (float) SIZE;
        
        this.text = text;
        this.textColor = textColor;
        this.font = new TrueTypeFont(new Font(fontName, style, SIZE), true);
        this.alignment = alignment;
    }
    
    public TrueTypeFont getFont()
    {
        return font;
    }
    
    public String getText()
    {
        return text;
    }
    
    public void setText(String text)
    {
        this.text = text;
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
        
    }
    
    @Override
    public void render()
    {
        textColor.set();
        font.drawString(x, y, text, sx, sy, alignment);
    }
}
