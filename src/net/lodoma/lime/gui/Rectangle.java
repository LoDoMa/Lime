package net.lodoma.lime.gui;

public final class Rectangle
{
    public final float x;
    public final float y;
    public final float w;
    public final float h;
    
    public Rectangle(float x, float y, float w, float h)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    
    public boolean isInside(float x, float y)
    {
        return x >= this.x && y >= this.y && x <= (this.x + w) && y <= (this.y + h);
    }
}
