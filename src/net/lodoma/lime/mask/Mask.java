package net.lodoma.lime.mask;

import org.lwjgl.opengl.GL11;

public abstract class Mask
{
    protected int displayList = 0;
    
    public Mask()
    {
        
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
        if(displayList == 0) compile();
        GL11.glCallList(displayList);
    }
    
    public void destroy()
    {
        if(displayList != 0) GL11.glDeleteLists(displayList, 1);
    }
}
