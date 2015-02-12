package net.lodoma.lime.gui.exp.clean;

import static org.lwjgl.opengl.GL11.*;
import net.lodoma.lime.gui.exp.UIObject;
import net.lodoma.lime.texture.Texture;
import net.lodoma.lime.util.Vector2;

public class CleanProgressBar extends UIObject
{
    private float progressVisual;
    
    public float progressValue;
    
    public CleanProgressBar(Vector2 position, Vector2 dimensions)
    {
        super();
        
        getLocalPosition().set(position);
        getLocalDimensions().set(dimensions);
        
        progressValue = 0.0f;
        progressVisual = progressValue;
    }

    @Override
    public void update(double timeDelta)
    {
        progressVisual += (progressValue - progressVisual) * timeDelta * 10.0f;
        
        super.update(timeDelta);
    }
    
    @Override
    public void render()
    {
        glPushMatrix();
        
        Vector2 position = getLocalPosition();
        glTranslatef(position.x, position.y, 0.0f);

        Vector2 dimensions = getLocalDimensions();
        
        Texture.NO_TEXTURE.bind();
        
        glBegin(GL_QUADS);
        {
            CleanUI.FOCUS_BODY_COLOR.setGL();
            glVertex2f(0.0f, 0.0f);
            glVertex2f(progressVisual * dimensions.x, 0.0f);
            glVertex2f(progressVisual * dimensions.x, dimensions.y);
            glVertex2f(0.0f, dimensions.y);
        }
        glEnd();
        
        glBegin(GL_LINES);
        {
            CleanUI.LINE_COLOR.setGL();
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
        
        super.render();
    }
}
