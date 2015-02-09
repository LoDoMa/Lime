package net.lodoma.lime.shader.light;

public class LightModifications
{
    public boolean positionModified;
    public boolean shapeModified;
    
    public LightData data;
    
    public LightModifications()
    {
        data = new LightData();
    }
    
    public void apply(LightData light)
    {
        if (positionModified)
            light.position.set(data.position);
        
        if (shapeModified)
        {
            light.radius = data.radius;
            light.color = data.color;
        }
    }
}
