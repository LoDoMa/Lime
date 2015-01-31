package net.lodoma.lime.gui.exp.clean;

import net.lodoma.lime.gui.exp.UIGroup;
import net.lodoma.lime.gui.exp.UIToggle;
import net.lodoma.lime.texture.Texture;
import net.lodoma.lime.util.TrueTypeFont;
import net.lodoma.lime.util.Vector2;
import static org.lwjgl.opengl.GL11.*;

public class CleanToggle extends UIToggle
{
    private float transparency;
    
    private CleanText text;
    
    public CleanToggle(Vector2 position, Vector2 dimensions, String text, UIGroup group)
    {
        super(group);
        
        this.position.set(position);
        this.dimensions.set(dimensions);
        
        children.add(this.text = new CleanText(new Vector2(position.x + dimensions.x / 2.0f, position.y), dimensions.y, text, TrueTypeFont.ALIGN_CENTER));
        
        transparency = 0.0f;
    }
    
    public void setText(String text)
    {
        this.text.text = text;
    }
    
    @Override
    public void update(double timeDelta)
    {
        super.update(timeDelta);
        
        if(mouseHovering || selected)
        {
            if(transparency < 1.0f)
            {
                transparency += timeDelta * CleanUI.FADE_IN_MULTIPLIER;
                if(transparency > 1.0f)
                    transparency = 1.0f;
            }
        }
        else
        {
            if(transparency > 0.0f)
            {
                transparency -= timeDelta * CleanUI.FADE_OUT_MULTIPLIER;
                if(transparency < 0.0f)
                    transparency = 0.0f;
            }
        }
    }
    
    @Override
    public void render()
    {
        if(transparency != 0.0f || selected)
        {
            glPushMatrix();
            glTranslatef(position.x, position.y, 0.0f);
            
            glBindTexture(GL_TEXTURE_2D, Texture.NO_TEXTURE);
            
            glBegin(GL_QUADS);
            {
                CleanUI.BODY_COLOR.setGL(transparency);
                glVertex2f(0.0f, 0.0f);
                glVertex2f(dimensions.x, 0.0f);
                glVertex2f(dimensions.x, dimensions.y);
                glVertex2f(0.0f, dimensions.y);
            }
            glEnd();
            
            glBegin(GL_LINES);
            {
                CleanUI.LINE_COLOR.setGL(transparency);
                glVertex2f(0.0f, 0.0f);
                glVertex2f(dimensions.x, 0.0f);
                glVertex2f(dimensions.x, 0.0f);
                glVertex2f(dimensions.x, dimensions.y);
                glVertex2f(dimensions.x, dimensions.y);
                glVertex2f(0.0f, dimensions.y);
                glVertex2f(0.0f, dimensions.y);
                glVertex2f(0.0f, 0.0f);
            }
            glEnd();
            
            glPopMatrix();
        }
        
        CleanUI.TEXT_COLOR.setGL();
        
        super.render();
    }
}
