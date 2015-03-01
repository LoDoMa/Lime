package net.lodoma.lime.gui.editor;

import net.lodoma.lime.gui.UIObject;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.util.Vector2;
import static org.lwjgl.opengl.GL11.*;

public class EditorContentPane extends UIObject
{
    private final Vector2 translation = new Vector2();
    private final Vector2 scale = new Vector2();
    
    public EditorContentPane(Vector2 translation, Vector2 scale)
    {
        super();
        this.translation.set(translation);
        this.scale.set(scale);
        
        /* The position is set to (0, 0) and dimensions are set to (0, 0) so that the pane's children
           behave like the content pane covers the entire window. */
        getLocalPosition().set(0, 0);
        getLocalDimensions().set(1, 1);
    }
    
    @Override
    public void update(double timeDelta)
    {
        /* The mouse position is modified to fit the content pane. */
        Vector2 originalMousePosition = Input.inputData.currentMousePosition.clone();
        Input.inputData.currentMousePosition.subLocal(translation);
        Input.inputData.currentMousePosition.divLocal(scale);
        
        super.update(timeDelta);

        /* When the children are updated, set the old mouse position */
        Input.inputData.currentMousePosition.set(originalMousePosition);
    }
    
    @Override
    public void render()
    {
        glPushMatrix();
        glTranslatef(translation.x, translation.y, 0.0f);
        glScalef(scale.x, scale.y, 0.0f);
        
        super.render();
        
        glPopMatrix();
    }
}
