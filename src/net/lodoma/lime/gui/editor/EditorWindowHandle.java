package net.lodoma.lime.gui.editor;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import net.lodoma.lime.gui.UICallback;
import net.lodoma.lime.gui.UIObject;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.resource.texture.Texture;
import net.lodoma.lime.util.Vector2;

class EditorWindowHandle extends UIObject
{
    private UICallback callback;
    
    private boolean mousePress;
    private int pressID;
    private boolean visible;
    
    public Vector2 lastPress;
    public Vector2 currentPress;
    
    public EditorWindowHandle(Vector2 position, Vector2 dimensions, boolean visible, UICallback callback)
    {
        super();
        getLocalPosition().set(position);
        getLocalDimensions().set(dimensions);
        
        this.visible = visible;
        this.callback = callback;
    }
    
    @Override
    public void update(double timeDelta)
    {
        currentPress = Input.getMousePosition();
        
        boolean currentHover = false;

        /* NOTE: We must clone the position because we getDimensions right after.
                 It might be possible to avoid that. */
        
        Vector2 position = getPosition().clone();
        Vector2 dimensions = getDimensions();
        if (currentPress.x >= position.x && currentPress.x <= position.x + dimensions.x &&
            currentPress.y >= position.y && currentPress.y <= position.y + dimensions.y)
            currentHover = true;
        
        for (int button = Input.MOUSE_BUTTON_1; button <= Input.MOUSE_BUTTON_8; button++)
            if (Input.getMouseDown(button) && currentHover)
            {
                mousePress = true;
                lastPress = currentPress;
                pressID = button;
            }
            else if (Input.getMouseUp(button) && button == pressID)
                mousePress = false;
        
        if (mousePress)
            callback.call();
        
        lastPress = currentPress;
        
        super.update(timeDelta);
    }
    
    @Override
    public void render()
    {
        if (visible)
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
        }
        
        super.render();
    }
}
