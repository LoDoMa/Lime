package net.lodoma.lime.gui;

import org.lwjgl.opengl.GL11;

public final class Color
{
    public final float r;
    public final float g;
    public final float b;
    public final float a;
    
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
    
    public void set()
    {
        GL11.glColor4f(r, g, b, a);
    }
    
    public void set(float a)
    {
        GL11.glColor4f(r, g, b, this.a * a);
    }
}
