package net.lodoma.lime.gui;

import java.awt.Font;

import net.lodoma.lime.input.Input;
import net.lodoma.lime.util.TrueTypeFont;
import net.lodoma.lime.util.Vector2;

import org.lwjgl.opengl.GL11;

public class Button implements GUIElement
{
    private Rectangle bounds;
    private Color bodyColor;
    private String text;
    private Color textColor;
    private String fontName;
    
    private Font font;
    private TrueTypeFont ttfont;
    
    public Button(Rectangle bounds, Color bodyColor, String text, Color textColor, String fontName)
    {
        this.bounds = bounds;
        this.bodyColor = bodyColor;
        this.text = text;
        this.textColor = textColor;
        this.fontName = fontName;
    }
    
    @Override
    public void create()
    {
        font = new Font(fontName, Font.BOLD, 32);
        ttfont = new TrueTypeFont(font, true);
    }
    
    @Override
    public void destroy()
    {
        ttfont.destroy();
    }
    
    @Override
    public void update(float timeDelta, Vector2 mousePosition)
    {
        if(Input.getMouseUp(Input.LEFT_MOUSE_BUTTON))
            if(bounds.inside(mousePosition.x, mousePosition.y))
            {
                // TODO: on click
            }
    }
    
    @Override
    public void render()
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(bounds.x, bounds.y, 1.0f);
        
        bodyColor.set();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(0.0f, 0.0f);
        GL11.glVertex2f(bounds.w, 0.0f);
        GL11.glVertex2f(bounds.w, bounds.h);
        GL11.glVertex2f(0.0f, bounds.h);
        GL11.glEnd();
        
        textColor.set();
        ttfont.drawString(bounds.w / 2, 0, text, (bounds.h * 0.75f) / 32, (bounds.h * 0.75f) / 32, TrueTypeFont.ALIGN_CENTER);
        
        GL11.glPopMatrix();
    }
}
