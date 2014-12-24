package net.lodoma.lime.world.entity;

import org.lwjgl.opengl.GL11;

import net.lodoma.lime.texture.Texture;
import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.util.Vector2;

public class Body implements Identifiable<Integer>
{
    public int identifier;

    public Vector2 oldPosition = new Vector2();
    public Vector2 oldVelocity = new Vector2();
    
    public Vector2 position = new Vector2();
    public Vector2 velocity = new Vector2();
    public float radius = 0.0f;
    public float density = 0.0f;
    public float mass = 0.0f;
    
    @Override
    public Integer getIdentifier()
    {
        return identifier;
    }
    
    @Override
    public void setIdentifier(Integer identifier)
    {
        this.identifier = identifier;
    }
    
    public boolean snapshotUpdate()
    {
        boolean changed = !oldPosition.equals(position) || !oldVelocity.equals(velocity);
        oldPosition.set(position);
        oldVelocity.set(velocity);
        return changed;
    }
    
    public void debugRender()
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(position.x, position.y, 0.0f);
        
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, Texture.NO_TEXTURE);
        
        GL11.glColor3f(1.0f, 0.0f, 0.0f);
        GL11.glBegin(GL11.GL_LINE_LOOP);

        for (int i = 0; i <= 10; i++)
        {
           float angle = (float) Math.toRadians(i * 360.0 / 10.0);
           GL11.glVertex2f((float) Math.cos(angle) * radius, (float) Math.sin(angle) * radius);
        }
      
        GL11.glEnd();
        
        GL11.glPopMatrix();
    }
}
