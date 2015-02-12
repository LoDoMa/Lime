package net.lodoma.lime.gui.exp.clean;

import net.lodoma.lime.gui.exp.UITextField;
import net.lodoma.lime.texture.Texture;
import net.lodoma.lime.util.TrueTypeFont;
import net.lodoma.lime.util.Vector2;
import static org.lwjgl.opengl.GL11.*;

public class CleanTextField extends UITextField
{
    private float transparency;
    
    public CleanTextField(Vector2 position, Vector2 dimensions, String text)
    {
        this(position, dimensions, text, TrueTypeFont.ALIGN_CENTER);
    }
    
    public CleanTextField(Vector2 position, Vector2 dimensions, String text, int alignment)
    {
        super(new CleanText(new Vector2(position.x + ((alignment == TrueTypeFont.ALIGN_LEFT) ?  (0) :
                                                     ((alignment == TrueTypeFont.ALIGN_RIGHT) ? (dimensions.x) :
                                                                                                (dimensions.x / 2.0f))), position.y), dimensions.y, text, alignment));
        this.position.set(position);
        this.dimensions.set(dimensions);
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

            Texture.NO_TEXTURE.bind();
            
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
        
        CleanUI.FOCUS_TEXT_COLOR.setGL();
        
        super.render();
    }
}
