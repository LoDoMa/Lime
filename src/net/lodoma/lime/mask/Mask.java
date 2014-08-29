package net.lodoma.lime.mask;

import net.lodoma.lime.util.Transform;
import net.lodoma.lime.util.Vector2;

import org.lwjgl.opengl.GL11;

public abstract class Mask
{
    private int displayList = 0;
    
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
    
    public void call(Transform transform)
    {
        Vector2 position = transform.getPosition();
        float angle = transform.getAngle();
        
        GL11.glPushMatrix();
        GL11.glTranslatef(position.x, position.y, 0);
        GL11.glRotatef(angle, 0, 0, 1);
        
        if(displayList == 0)
            compile();
        GL11.glCallList(displayList);
        
        GL11.glPopMatrix();
    }
    
    public void destroy()
    {
        if(displayList != 0)
            GL11.glDeleteLists(displayList, 1);
    }
}
