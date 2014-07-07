package net.lodoma.lime.mask;

import org.lwjgl.opengl.GL11;

public abstract class Mask
{
    protected int displayList;
    
    public Mask()
    {        
        displayList = GL11.glGenLists(1);
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
        GL11.glCallList(displayList);
    }
    
    public void destroy()
    {
        GL11.glDeleteLists(displayList, 1);
    }
}
