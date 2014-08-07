package net.lodoma.lime.gui.exp;

import net.lodoma.lime.gui.Color;
import net.lodoma.lime.util.Vector2;

public abstract class GShape
{
    private static final Vector2 DEFAULT_TRANSLATION = new Vector2(0.0f, 0.0f);
    private static final Color DEFAULT_COLOR = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    
    private final Vector2 translation = DEFAULT_TRANSLATION.clone();
    private final Color color = DEFAULT_COLOR.clone();

    private float rotation = 0.0f;
    
    public Vector2 getTranslation()
    {
        return translation;
    }

    public float getRotation()
    {
        return rotation;
    }

    public void setRotation(float rotation)
    {
        this.rotation = rotation;
    }

    public Color getColor()
    {
        return color;
    }
    
    public boolean isPointInside(Vector2 point)
    {
        return isPointInside(point.x, point.y);
    }
    
    public abstract boolean isPointInside(float x, float y);

    public abstract void render();
}
