package net.lodoma.lime.shader.light;

import net.lodoma.lime.gui.Color;
import net.lodoma.lime.util.Vector2;

public class LightData
{
    public Vector2 position;
    public float radius;
    
    public Color color;
    
    public float angleRangeBegin;
    public float angleRangeEnd;
    
    public LightData()
    {
        position = new Vector2(0.0f, 0.0f);
        color = new Color(0.0f, 0.0f, 0.0f, 0.0f);
    }
}
