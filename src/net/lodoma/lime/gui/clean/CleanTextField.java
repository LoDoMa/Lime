package net.lodoma.lime.gui.clean;

import net.lodoma.lime.gui.UITextField;
import net.lodoma.lime.resource.texture.Texture;
import net.lodoma.lime.util.Vector2;
import static org.lwjgl.opengl.GL11.*;

public class CleanTextField extends UITextField
{
    private float transparency;
    
    public CleanTextField(Vector2 position, Vector2 dimensions, String text, int alignment)
    {
        super(new CleanText(dimensions, text, CleanUI.FOCUS_TEXT_COLOR, alignment));
        getLocalPosition().set(position);
        getLocalDimensions().set(dimensions);
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
            
            Vector2 position = getPosition();
            glTranslatef(position.x, position.y, 0.0f);

            Vector2 dimensions = getDimensions();

            Texture.NO_TEXTURE.bind(0);
            
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
        
        super.render();
    }
}
