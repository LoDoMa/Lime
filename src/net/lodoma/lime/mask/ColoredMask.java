package net.lodoma.lime.mask;

import org.lwjgl.opengl.GL11;

public class ColoredMask extends Mask
{
    protected int n;
    
    protected float[] x;
    protected float[] y;
    
    protected float[] r;
    protected float[] g;
    protected float[] b;
    protected float[] a;
    
    private ColoredMask()
    {
        
    }
    
    public ColoredMask(int n, float[] x, float[] y, float[] r, float[] g, float[] b, float[] a)
    {
        this.n = n;
        
        this.x = x;
        this.y = y;
        
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
    
    @Override
    public Mask newCopy()
    {
        ColoredMask copy = new ColoredMask();
        copy.n = n;
        
        copy.x = new float[copy.n];
        copy.y = new float[copy.n];
        
        copy.r = new float[copy.n];
        copy.g = new float[copy.n];
        copy.b = new float[copy.n];
        copy.a = new float[copy.n];

        System.arraycopy(x, 0, copy.x, 0, copy.n);
        System.arraycopy(y, 0, copy.y, 0, copy.n);

        System.arraycopy(r, 0, copy.r, 0, copy.n);
        System.arraycopy(g, 0, copy.g, 0, copy.n);
        System.arraycopy(b, 0, copy.b, 0, copy.n);
        System.arraycopy(a, 0, copy.a, 0, copy.n);
        
        return copy;
    }
    
    @Override
    public void render()
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL11.glBegin(GL11.GL_POLYGON);
        for(int i = 0; i < n; i++)
        {
            GL11.glColor4f(r[i], g[i], b[i], a[i]);
            GL11.glVertex2f(x[i], y[i]);
        }
        GL11.glEnd();
    }
}
