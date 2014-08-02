package net.lodoma.lime.mask;

import org.lwjgl.opengl.GL11;

public class ColoredMask extends Mask
{
    public static final int CIRCLE_VERTEX_COUNT = 16;
    
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
    
    public ColoredMask(float radius, float r, float g, float b, float a)
    {
        this.n = CIRCLE_VERTEX_COUNT;
        
        this.x = new float[CIRCLE_VERTEX_COUNT];
        this.y = new float[CIRCLE_VERTEX_COUNT];
        
        this.r = new float[CIRCLE_VERTEX_COUNT];
        this.g = new float[CIRCLE_VERTEX_COUNT];
        this.b = new float[CIRCLE_VERTEX_COUNT];
        this.a = new float[CIRCLE_VERTEX_COUNT];
        
        for(int i = 0; i < CIRCLE_VERTEX_COUNT; i++)
        {
            float angle = 2.0f * (float) Math.PI * i / (float) CIRCLE_VERTEX_COUNT;
            float x = (float) Math.cos(angle);
            float y = (float) Math.sin(angle);
            
            this.x[i] = x * radius;
            this.y[i] = y * radius;
            
            this.r[i] = r;
            this.g[i] = g;
            this.b[i] = b;
            this.a[i] = a;
        }
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
