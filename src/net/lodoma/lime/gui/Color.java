package net.lodoma.lime.gui;

import org.lwjgl.opengl.GL11;

public final class Color
{
    private float r;
    private float g;
    private float b;
    private float a;
    
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
    
    public float getR()
    {
        return r;
    }

    public void setR(float r)
    {
        this.r = r;
    }

    public float getG()
    {
        return g;
    }

    public void setG(float g)
    {
        this.g = g;
    }

    public float getB()
    {
        return b;
    }

    public void setB(float b)
    {
        this.b = b;
    }

    public float getA()
    {
        return a;
    }

    public void setA(float a)
    {
        this.a = a;
    }
    
    public void set(float r, float g, float b)
    {
        set(r, g, b, this.a);
    }
    
    public void set(Color color)
    {
        set(color.r, color.g, color.b, color.a);
    }
    
    public void setWOA(Color color)
    {
        set(color.r, color.g, color.b, this.a);
    }
    
    public void set(float r, float g, float b, float a)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public void setGL()
    {
        GL11.glColor4f(r, g, b, a);
    }
    
    public void setGL(float a)
    {
        GL11.glColor4f(r, g, b, this.a * a);
    }
    
    public Color clone()
    {
        return new Color(r, g, b, a);
    }
}
