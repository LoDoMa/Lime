package net.joritan.jlime.gui;

public class GColor
{
    public final float r;
    public final float g;
    public final float b;
    
    public GColor(float r, float g, float b)
    {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    public GColor(int r, int g, int b)
    {
        this(r / 255.0f, g / 255.0f, b / 255.0f);
    }
}
