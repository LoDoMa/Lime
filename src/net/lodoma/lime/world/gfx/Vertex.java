package net.lodoma.lime.world.gfx;

import net.lodoma.lime.util.Color;
import net.lodoma.lime.util.Vector2;

public class Vertex
{
    public static final int stride = 8 * 4;
    public static final int vertexOffset = 0;
    public static final int colorOffset = 2 * 4;
    public static final int textureOffset = 6 * 4;
    
    public float x, y;
    public float r, g, b, a;
    public float s, t;
    public String texture;
    
    public Vertex setXY(float x, float y)
    {
        this.x = x;
        this.y = y;
        return this;
    }
    
    public Vertex setXY(Vector2 v)
    {
        return setXY(v.x, v.y);
    }
    
    public Vertex setRGBA(float r, float g, float b, float a)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        return this;
    }
    
    public Vertex setRGBA(Color color)
    {
        return setRGBA(color.r, color.g, color.b, color.a);
    }
    
    public Vertex setST(float s, float t)
    {
        this.s = s;
        this.t = t;
        return this;
    }
    
    public Vertex setTexture(String texture)
    {
        this.texture = texture;
        return this;
    }
}
