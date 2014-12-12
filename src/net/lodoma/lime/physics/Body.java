package net.lodoma.lime.physics;

import org.lwjgl.opengl.GL11;

import net.lodoma.lime.texture.Texture;
import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.util.Vector2;

// NOTE: Make body abstract; allow for different component types

public class Body implements Identifiable<Integer>
{
    public static enum BodyType
    {
        CIRCLE,
    }
    
    public int identifier;
    public PhysicsWorld world;
    
    public Vector2 position;
    public Vector2 velocity;
    public float radius;
    
    public float restitution;
    public float density;

    public float volume;
    public float mass;
    
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
    
    public void simulate(float timeDelta)
    {
        position.addLocal(velocity.mul(timeDelta));
    }
    
    public void debugRender()
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(position.x, position.y, 0.0f);
        
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
