package net.lodoma.lime.gui.clean;

import net.lodoma.lime.gui.UIAbstractButton;
import net.lodoma.lime.gui.UICallback;
import net.lodoma.lime.texture.Texture;
import net.lodoma.lime.util.Vector2;
import static org.lwjgl.opengl.GL11.*;

public class CleanButton extends UIAbstractButton
{
    private float transparency;
    
    private CleanText text;
    
    public CleanButton(Vector2 position, Vector2 dimensions, String text, int alignment, UICallback onClick)
    {
        super(onClick);
        getLocalPosition().set(position);
        getLocalDimensions().set(dimensions);
        
        addChild(this.text = new CleanText(dimensions, text, CleanUI.TEXT_COLOR, alignment));
        
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
        
        if(mouseHovering)
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
        if(transparency != 0.0f)
        {
            glPushMatrix();
            
            Vector2 position = getPosition();
            glTranslatef(position.x, position.y, 0.0f);
            
            Vector2 dimensions = getDimensions();
            
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
        
        super.render();
    }
}
