package net.joritan.jlime.gui;

import java.security.InvalidParameterException;

import net.joritan.jlime.stage.root.BlueScreen;

public class GShape
{
    private int n;
    private float[] x;
    private float[] y;
    
    public GShape(int n, float[] x, float[] y)
    {
        this.n = n;
        this.x = new float[this.n];
        this.y = new float[this.n];
        if (x.length < this.n)
            new BlueScreen(null, new InvalidParameterException("vertex count is greater than x coordinate array length"));
        System.arraycopy(x, 0, this.x, 0, this.n);
        if (y.length < this.n)
            new BlueScreen(null, new InvalidParameterException("vertex count is greater than y coordinate array length"));
        System.arraycopy(y, 0, this.y, 0, this.n);
    }
    
    public GShape(float w, float h)
    {
        this(4, new float[] {0, w, w, 0}, new float[] {0, 0, h, h});
    }
    
    public GShape(float x1, float y1, float x2, float y2)
    {
        this(4, new float[] {x1, x2, x2, x1}, new float[] {y1, y1, y2, y2});
    }
    
    public GShape(GShape copy)
    {
        this.n = copy.n;
        this.x = new float[this.n];
        this.y = new float[this.n];
        System.arraycopy(copy.x, 0, this.x, 0, this.n);
        System.arraycopy(copy.y, 0, this.y, 0, this.n);
    }
    
    public boolean contains(float x, float y)
    {
        boolean inside = false;
        for (int i = 0, j = this.n - 1; i < this.n; j = i++)
        {
            if (((this.y[i] > y) != (this.y[j] > y)) && (x < (this.x[j] - this.x[i]) * (y - this.y[i]) / (this.y[j] - this.y[i]) + this.x[i]))
                inside = !inside;
        }
        return inside;
    }
    
    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        return new GShape(this);
    }
}
