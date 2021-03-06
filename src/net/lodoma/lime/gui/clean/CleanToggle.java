package net.lodoma.lime.gui.clean;

import net.lodoma.lime.gui.UIGroup;
import net.lodoma.lime.gui.UIGroupMember;
import net.lodoma.lime.resource.texture.Texture;
import net.lodoma.lime.util.Vector2;
import static org.lwjgl.opengl.GL11.*;

public class CleanToggle extends UIGroupMember
{
    private float transparency;
    
    private CleanText text;
    
    @SuppressWarnings("rawtypes")
    public CleanToggle(Vector2 position, Vector2 dimensions, String text, UIGroup group, int alignment)
    {
        super(group);
        
        getLocalPosition().set(position);
        getLocalDimensions().set(dimensions);
        
        addChild(this.text = new CleanText(dimensions, text, CleanUI.TEXT_COLOR, alignment));
        
        transparency = 0.0f;
    }
    
    public String getText()
    {
        return text.text;
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
        
        CleanUI.TEXT_COLOR.setGL();
        
        super.render();
    }
}
