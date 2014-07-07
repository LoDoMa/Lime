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
    
    public ColoredMask(int n, float[] x, float[] y, float[] r, float[] g, float[] b)
    {
        this.n = n;
        
        this.x = x;
        this.y = y;
        
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    @Override
    public void render()
    {
        GL11.glBegin(GL11.GL_POLYGON);
        for(int i = 0; i < n; i++)
        {
            GL11.glColor3f(r[i], g[i], b[i]);
            GL11.glVertex2f(x[i], y[i]);
        }
        GL11.glEnd();
    }
}
