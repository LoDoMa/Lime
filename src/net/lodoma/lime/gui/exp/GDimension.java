package net.lodoma.lime.gui.exp;

public class GDimension
{
    private float width;
    private float height;
    
    public GDimension()
    {
        this(0.0f, 0.0f);
    }
    
    public GDimension(GDimension other)
    {
        this(other.width, other.height);
    }
    
    public GDimension(float width, float height)
    {
        this.width = width;
        this.height = height;
    }
    
    public float getWidth()
    {
        return width;
    }

    public void setWidth(float width)
    {
        this.width = width;
    }

    public float getHeight()
    {
        return height;
    }

    public void setHeight(float height)
    {
        this.height = height;
    }
    
    public void set(float width, float height)
    {
        setWidth(width);
        setHeight(height);
    }
    
    public void set(GDimension dimension)
    {
        setWidth(dimension.width);
        setHeight(dimension.height);
    }

    @Override
    public String toString()
    {
        return "dim(" + width + "x" + height + ")";
    }
}
