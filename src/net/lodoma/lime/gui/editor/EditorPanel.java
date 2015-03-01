package net.lodoma.lime.gui.editor;

import net.lodoma.lime.gui.UIObject;
import net.lodoma.lime.gui.editor.layout.EditorPanelHorizontalLayout;
import net.lodoma.lime.texture.Texture;
import net.lodoma.lime.util.Vector2;

import static org.lwjgl.opengl.GL11.*;

public class EditorPanel extends UIObject
{
    public EditorPanel(Vector2 position, Vector2 dimensions)
    {
        super();
        getLocalPosition().set(position);
        getLocalDimensions().set(dimensions);
        
        setLayout(new EditorPanelHorizontalLayout(new Vector2(0.005f, 0.005f)));
    }
    
    @Override
    public void render()
    {
        glPushMatrix();
        
        Vector2 position = getPosition();
        glTranslatef(position.x, position.y, 0.0f);
        
        Vector2 dimensions = getDimensions();
        
        Texture.NO_TEXTURE.bind();
        
        glBegin(GL_QUADS);
        {
            EditorUI.BACKGROUND.setGL();
            glVertex2f(0.0f, 0.0f);
            glVertex2f(dimensions.x, 0.0f);
            glVertex2f(dimensions.x, dimensions.y);
            glVertex2f(0.0f, dimensions.y);
        }
        glEnd();
        
        glPopMatrix();
        
        super.render();
    }
}
