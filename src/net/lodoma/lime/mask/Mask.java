package net.lodoma.lime.mask;

import net.lodoma.lime.util.Vector2;

import org.lwjgl.opengl.GL11;

public abstract class Mask
{
    protected float translateX;
    protected float translateY;
    protected float rotate;
    
    protected int displayList = 0;
    
    public Mask()
    {
        
    }
    
    public Vector2 getTranslation()
    {
        return new Vector2(translateX, translateY);
    }
    
    public void setTranslation(float x, float y)
    {
        translateX = x;
        translateY = y;
    }
    
    public float getRotation()
    {
        return rotate;
    }
    
    public void setRotation(float r)
    {
        rotate = (float) Math.toDegrees(r);
    }
    
    public void compile()
    {
        if(displayList == 0)
            displayList = GL11.glGenLists(1);
        
        GL11.glNewList(displayList, GL11.GL_COMPILE);
        render();
        GL11.glEndList();
    }
    
    public abstract void render();
    
    public void call()
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(translateX, translateY, 0);
        GL11.glRotatef(rotate, 0, 0, 1);
        if(displayList == 0) compile();
        GL11.glCallList(displayList);
        GL11.glPopMatrix();
    }
    
    public void destroy()
    {
        if(displayList != 0) GL11.glDeleteLists(displayList, 1);
    }
}
