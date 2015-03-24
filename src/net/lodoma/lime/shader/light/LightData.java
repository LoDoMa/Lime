package net.lodoma.lime.shader.light;

import net.lodoma.lime.util.Color;
import net.lodoma.lime.util.Vector2;

public class LightData
{
    public Vector2 position;
    public float radius;
    public Color color;
    
    public LightData()
    {
        position = new Vector2(0.0f, 0.0f);
        color = new Color(0.0f, 0.0f, 0.0f, 0.0f);
    }
    
    public LightData(LightData data)
    {
        position = new Vector2(data.position);
        radius = data.radius;
        color = new Color(data.color);
    }
}
