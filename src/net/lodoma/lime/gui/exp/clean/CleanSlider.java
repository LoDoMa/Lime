package net.lodoma.lime.gui.exp.clean;

import net.lodoma.lime.gui.exp.UIAbstractButton;
import net.lodoma.lime.gui.exp.UICallback;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.texture.Texture;
import net.lodoma.lime.util.Vector2;
import static org.lwjgl.opengl.GL11.*;

public class CleanSlider extends UIAbstractButton
{
    private float transparency;
    private float sliderVisual;
    
    public UICallback onSlide;
    public float sliderValue;
    
    public CleanSlider(Vector2 position, Vector2 dimensions, UICallback slideCallback)
    {
        super(null);
        getLocalPosition().set(position);
        getLocalDimensions().set(dimensions);
        
        onSlide = slideCallback;
        
        sliderValue = 0.0f;
        sliderVisual = sliderValue;
        
        onClick = new UICallback()
        {
            @Override
            public void call()
            {
                sliderValue = (Input.getMousePosition().x - position.x) / dimensions.x;
                onSlide.call();
            }
        };
    }

    @Override
    public void update(double timeDelta)
    {
        super.update(timeDelta);
        
        sliderVisual += (sliderValue - sliderVisual) * timeDelta * 10.0f;
        
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
        glPushMatrix();
        
        Vector2 position = getPosition();
        glTranslatef(position.x, position.y, 0.0f);

        Vector2 dimensions = getDimensions();
        
        Texture.NO_TEXTURE.bind();
        
        if(transparency != 0.0f)
        {
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
        }

        glBegin(GL_LINES);
        {
            CleanUI.FOCUS_LINE_COLOR.setGL();
            glVertex2f(0.0f, dimensions.y / 2);
            glVertex2f(dimensions.x, dimensions.y / 2);
        }
        glEnd();
        
        glBegin(GL_QUADS);
        {
            CleanUI.FOCUS_BODY_COLOR.setGL();
            glVertex2f(sliderVisual * dimensions.x - 0.005f, 0.0f);
            glVertex2f(sliderVisual * dimensions.x + 0.005f, 0.0f);
            glVertex2f(sliderVisual * dimensions.x + 0.005f, dimensions.y);
            glVertex2f(sliderVisual * dimensions.x - 0.005f, dimensions.y);
        }
        glEnd();
        
        glPopMatrix();
        
        super.render();
    }
}
