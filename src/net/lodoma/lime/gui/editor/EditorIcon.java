package net.lodoma.lime.gui.editor;

import net.lodoma.lime.gui.UIAbstractButton;
import net.lodoma.lime.gui.UICallback;
import net.lodoma.lime.texture.Texture;
import net.lodoma.lime.util.Vector2;

import static org.lwjgl.opengl.GL11.*;

public class EditorIcon extends UIAbstractButton
{
    private Texture texture;
    
    public EditorIcon(Vector2 position, Vector2 dimensions, Texture texture, UICallback callback)
    {
        super(callback);
        getLocalPosition().set(position);
        getLocalDimensions().set(dimensions);
        
        this.texture = texture;
    }
    
    @Override
    public void render()
    {
        glPushMatrix();
        
        Vector2 position = getPosition();
        glTranslatef(position.x, position.y, 0.0f);
        
        Vector2 dimensions = getDimensions();

        // Draw body
        
        Texture.NO_TEXTURE.bind(0);
        EditorUI.BACKGROUND_FOCUS.setGL();
        
        glBegin(GL_QUADS);
        {
            glVertex2f(0.0f, 0.0f);
            glVertex2f(dimensions.x, 0.0f);
            glVertex2f(dimensions.x, dimensions.y);
            glVertex2f(0.0f, dimensions.y);
        }
        glEnd();
        
        // Draw picture
        
        texture.bind(0);
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

        glBegin(GL_QUADS);
        {
            glTexCoord2f(0.0f, 1.0f); glVertex2f(0.0f, 0.0f);
            glTexCoord2f(1.0f, 1.0f); glVertex2f(dimensions.x, 0.0f);
            glTexCoord2f(1.0f, 0.0f); glVertex2f(dimensions.x, dimensions.y);
            glTexCoord2f(0.0f, 0.0f); glVertex2f(0.0f, dimensions.y);
        }
        glEnd();
        
        // Draw selection overlay
        
        if (mouseHovering)
        {
            Texture.NO_TEXTURE.bind(0);
            EditorUI.BACKGROUND_SELETED.setGL();
            
            glBegin(GL_QUADS);
            {
                glVertex2f(0.0f, 0.0f);
                glVertex2f(dimensions.x, 0.0f);
                glVertex2f(dimensions.x, dimensions.y);
                glVertex2f(0.0f, dimensions.y);
            }
            glEnd();
        }
            
        glPopMatrix();
        
        super.render(); // Does nothing
    }
}
