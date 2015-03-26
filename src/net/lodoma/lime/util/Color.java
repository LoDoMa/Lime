package net.lodoma.lime.util;

import static org.lwjgl.opengl.GL11.*;

public class Color
{
    public float r;
    public float g;
    public float b;
    public float a;
    
    public Color()
    {
        this(0.0f, 0.0f, 0.0f, 0.0f);
    }
    
    public Color(float r, float g, float b)
    {
        this(r, g, b, 1.0f);
    }
    
    public Color(float r, float g, float b, float a)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
    
    public Color(Color other)
    {
        r = other.r;
        g = other.g;
        b = other.b;
        a = other.a;
    }
    
    public void set(float r, float g, float b, float a)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
    
    public void set(float r, float g, float b)
    {
        set(r, g, b, a);
    }
    
    public void set(Color other)
    {
        r = other.r;
        g = other.g;
        b = other.b;
        a = other.a;
    }
    
    public void setGL()
    {
        setGL(1.0f, 1.0f);
    }
    
    public void setGL(float alphaMul)
    {
        setGL(1.0f, alphaMul);
    }
    
    public void setGL(float colorMul, float alphaMul)
    {
        glColor4f(r * colorMul, g * colorMul, b * colorMul, a * alphaMul);
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Color))
            return false;
        Color c = (Color) obj;
        if (r != c.r) return false;
        if (g != c.g) return false;
        if (b != c.b) return false;
        if (a != c.a) return false;
        return true;
    }
    
    @Override
    protected Color clone()
    {
        return new Color(r, g, b, a);
    }
}
