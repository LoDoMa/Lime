package net.lodoma.lime.gui;

import java.awt.Font;

import org.lwjgl.opengl.GL11;

import net.lodoma.lime.util.TrueTypeFont;
import net.lodoma.lime.util.Vector2;

public abstract class TextButton extends Button
{
    protected Color textColor;
    protected String text;
    protected TrueTypeFont font;
    protected int alignment;
    
    public TextButton(Rectangle bounds, Color textColor, String text, String fontName, int alignment)
    {
        super(bounds);
        this.textColor = textColor;
        this.text = text;
        this.font = new TrueTypeFont(new Font(fontName, Font.PLAIN, 32), true);
        this.alignment = alignment;
    }
    
    public TextButton(Rectangle bounds, Color textColor, String text, String fontName)
    {
        this(bounds, textColor, text, fontName, TrueTypeFont.ALIGN_CENTER);
    }
    
    @Override
    public void update(float timeDelta, Vector2 mousePosition)
    {
        super.update(timeDelta, mousePosition);
    }
    
    @Override
    public void render()
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(bounds.x, bounds.y, 1.0f);

        textColor.set();
        font.drawString(bounds.w / 2, 0, text, (bounds.h * 0.75f) / 32, (bounds.h * 0.75f) / 32, alignment);
        
        GL11.glPopMatrix();
    }
}
