package net.lodoma.lime.physics;

import org.lwjgl.opengl.GL11;

import net.lodoma.lime.texture.Texture;
import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.util.Vector2;

public class ShapeComponent implements Identifiable<Integer>
{
    public static enum ComponentType
    {
        CIRCLE,
    }
    
    public int identifier;
    public Shape shape;
    public Vector2 position;
    public float rotation;
    
    public float radius;
    
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
    
    public void render()
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(position.x, position.y, 0.0f);
        GL11.glRotatef(rotation, 0.0f, 0.0f, 1.0f);
        
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, Texture.NO_TEXTURE);
        
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);

        GL11.glVertex2f(0.0f, 0.0f);
        for (int i=0; i <= 10; i++)
        {
           float angle = (float) Math.toRadians(i * 360.0 / 10.0);
           GL11.glVertex2f((float) Math.cos(angle) * radius, (float) Math.sin(angle) * radius);
        }
      
        GL11.glEnd();
        
        GL11.glPopMatrix();
    }
}
