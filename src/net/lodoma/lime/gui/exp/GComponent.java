package net.lodoma.lime.gui.exp;

import net.lodoma.lime.gui.Color;
import net.lodoma.lime.util.Vector2;

import static org.lwjgl.opengl.GL11.*;

public class GComponent
{
    private static final Vector2 DEFAULT_TRANSLATION = new Vector2(0.0f, 0.0f);
    private static final Color DEFAULT_COLOR = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    private static final float DEFAULT_ROTATION = 0.0f;
    private static final GActionListener DEFAULT_ACTION_LISTENER = new GActionListener() {};
    
    protected final Vector2 translation = DEFAULT_TRANSLATION.clone();
    protected final Color color = DEFAULT_COLOR.clone();
    
    protected float rotation = DEFAULT_ROTATION;
    protected GShape shape = new GNullShape();
    protected GActionListener actionListener = DEFAULT_ACTION_LISTENER;
    
    public Vector2 getTranslation()
    {
        return translation;
    }

    public Color getColor()
    {
        return color;
    }
    
    public float getRotation()
    {
        return rotation;
    }

    public void setRotation(float rotation)
    {
        this.rotation = rotation;
    }

    public GShape getShape()
    {
        return shape;
    }

    public void setShape(GShape shape)
    {
        if(shape == null) this.shape = new GNullShape();
        else this.shape = shape;
    }
    
    public GActionListener getActionListener()
    {
        return actionListener;
    }
    
    public void setActionListener(GActionListener actionListener)
    {
        if(actionListener == null) this.actionListener = DEFAULT_ACTION_LISTENER;
        else this.actionListener = actionListener;
    }

    public void update()
    {
        
    }
    
    public void render()
    {
        color.setGL();
        
        glPushMatrix();
        {
            glTranslatef(translation.x, translation.y, 0.0f);
            glRotatef(rotation, 0.0f, 0.0f, 1.0f);
            
            shape.render();
        }
        glPopMatrix();
    }
}
