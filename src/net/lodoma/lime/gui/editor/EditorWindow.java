package net.lodoma.lime.gui.editor;

import net.lodoma.lime.gui.UICallback;
import net.lodoma.lime.gui.UIObject;
import net.lodoma.lime.gui.clean.CleanText;
import net.lodoma.lime.texture.Texture;
import net.lodoma.lime.util.Color;
import net.lodoma.lime.util.TrueTypeFont;
import net.lodoma.lime.util.Vector2;
import static org.lwjgl.opengl.GL11.*;

public class EditorWindow extends UIObject
{
    public EditorPanel panel;
    public EditorContentPane contentPane;
    private EditorWindowHandle handle;
    
    public EditorWindow(Vector2 position, Vector2 dimensions, String title)
    {
        super();
        getLocalPosition().set(position);
        getLocalDimensions().set(dimensions.x, dimensions.y + 0.03f);

        panel = new EditorPanel(new Vector2(0.0f, dimensions.y), new Vector2(dimensions.x, 0.03f));
        contentPane = new EditorContentPane(new Vector2(0), dimensions);
        handle = new EditorWindowHandle(new Vector2(0.0f, dimensions.y), new Vector2(dimensions.x, 0.03f), new UICallback()
        {
            @Override
            public void call()
            {
                Vector2 diff = handle.currentPress.sub(handle.lastPress);
                getLocalPosition().addLocal(diff);
            }
        });
        
        panel.addChild(new CleanText(0.025f, " " + title, new Color(1.0f, 1.0f, 1.0f, 1.0f), TrueTypeFont.ALIGN_LEFT));
        
        addChild(panel);
        addChild(contentPane);
        addChild(handle);
    }
    
    @Override
    public void render()
    {
        super.render();
        
        glPushMatrix();
        
        Vector2 position = getPosition();
        glTranslatef(position.x, position.y, 0.0f);
        
        Vector2 dimensions = getDimensions();
        
        Texture.NO_TEXTURE.bind(0);
        EditorUI.BACKGROUND_FOCUS.setGL();
        
        glBegin(GL_LINES);
        {
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
}
