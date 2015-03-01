package net.lodoma.lime.gui.editor;

import net.lodoma.lime.gui.UIObject;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.util.Vector2;
import static org.lwjgl.opengl.GL11.*;

public class EditorContentPane extends UIObject
{
    public EditorContentPane(Vector2 position, Vector2 dimensions)
    {
        super();
        getLocalPosition().set(position);
        getLocalDimensions().set(dimensions);
    }

    /* A lot of Vector2 objects are instantiated during execution.
       This helps lower that number down. It doesn't help thread
       safety. UI isn't thread safe. */
    private final Vector2 cacheVector = new Vector2();
    
    @Override
    public Vector2 getPosition()
    {
        cacheVector.set(0.0f, 0.0f);
        return cacheVector;
    }
    
    @Override
    public Vector2 getDimensions()
    {
        cacheVector.set(1.0f, 1.0f);
        return cacheVector;
    }
    
    private Vector2 getParentPosition()
    {
        cacheVector.set(0.0f, 0.0f);
        if (getParent() != null)
            cacheVector.addLocal(getParent().getPosition());
        return cacheVector;
    }
    
    @Override
    public void update(double timeDelta)
    {
        /* The mouse position is modified to fit the content pane. */
        Vector2 originalMousePosition = Input.inputData.currentMousePosition.clone();
        
        Vector2 position = getParentPosition();
        Input.inputData.currentMousePosition.subLocal(position);
        Input.inputData.currentMousePosition.divLocal(getLocalDimensions());
        
        super.update(timeDelta);

        /* When the children are updated, set the old mouse position */
        Input.inputData.currentMousePosition.set(originalMousePosition);
    }
    
    @Override
    public void render()
    {
        glPushMatrix();
        Vector2 position = getParentPosition();
        glTranslatef(position.x, position.y, 0.0f);
        Vector2 scale = getLocalDimensions();
        glScalef(scale.x, scale.y, 0.0f);
        
        super.render();
        
        glPopMatrix();
    }
}
