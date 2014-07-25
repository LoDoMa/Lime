package net.lodoma.lime.client.stage.mainmenu;

import java.awt.Font;

import net.lodoma.lime.gui.Button;
import net.lodoma.lime.gui.Color;
import net.lodoma.lime.gui.Rectangle;
import net.lodoma.lime.gui.Text;
import net.lodoma.lime.util.TrueTypeFont;
import net.lodoma.lime.util.Vector2;

import org.lwjgl.opengl.GL11;

public class MainMenuButton extends Button
{
    private static final String FONT_NAME = "My type of font";
    private static final Color TEXT_COLOR = new Color(1.0f, 1.0f, 1.0f);
    private static final Color BODY_COLOR = new Color(0.2f, 0.2f, 0.2f, 0.2f);
    private static final Color OUTLINE_COLOR = new Color(1.0f, 1.0f, 1.0f);
    
    private Text text;
    private float hoverTransparency = 0.0f;
    
    public MainMenuButton(Rectangle bounds, String text)
    {
        super(bounds);
        this.text = new Text(bounds.w / 2.0f, 0.0f, (bounds.h * 0.75f), (bounds.h * 0.75f), text, TEXT_COLOR, FONT_NAME, Font.PLAIN, TrueTypeFont.ALIGN_CENTER);
    }
    
    @Override
    public void update(double timeDelta, Vector2 mousePosition)
    {
        super.update(timeDelta, mousePosition);
        
        if(mouseHover)
        {
            if(hoverTransparency < 1.0f)
            {
                hoverTransparency += timeDelta * 5.0f;
                if(hoverTransparency > 1.0f)
                    hoverTransparency = 1.0f;
            }
        }
        else
            hoverTransparency = 0.0f;
    }
    
    @Override
    public void render()
    {
        GL11.glPushMatrix();
        
        GL11.glTranslatef(bounds.x, bounds.y, 1.0f);
        
        if(hoverTransparency != 0.0f)
        {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            
            GL11.glBegin(GL11.GL_LINES);
            {
                OUTLINE_COLOR.set();
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
            
            GL11.glBegin(GL11.GL_QUADS);
            {
                BODY_COLOR.set(hoverTransparency);
                GL11.glVertex2f(0.0f, 0.0f);
                GL11.glVertex2f(bounds.w, 0.0f);
                GL11.glVertex2f(bounds.w, bounds.h);
                GL11.glVertex2f(0.0f, bounds.h);
            }
            GL11.glEnd();
        }
        
        text.render();
        
        GL11.glPopMatrix();
        
        super.render();
    }
}
