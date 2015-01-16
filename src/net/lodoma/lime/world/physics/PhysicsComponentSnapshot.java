package net.lodoma.lime.world.physics;

import org.lwjgl.opengl.GL11;

import net.lodoma.lime.texture.Texture;
import net.lodoma.lime.util.Vector2;

public class PhysicsComponentSnapshot
{
    public Vector2 position;
    public float angle;
    
    public PhysicsComponentShapeType type;
    public float radius;
    public Vector2[] vertices;
    
    public void debugRender()
    {
        switch(type)
        {
        case CIRCLE:
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(position.x, position.y, 0.0f);
            GL11.glRotatef(angle, 0.0f, 0.0f, 1.0f);
            GL11.glScalef(radius, radius, 1.0f);
            
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, Texture.NO_TEXTURE);

            GL11.glColor3f(1.0f, 1.0f, 1.0f);
            GL11.glBegin(GL11.GL_TRIANGLE_FAN);

            GL11.glVertex2f(0.0f, 0.0f);
            for (int i = 0; i <= 10; i++)
            {
               float angle = (float) Math.toRadians(i * 360.0 / 10.0);
               GL11.glVertex2f((float) Math.cos(angle), (float) Math.sin(angle));
            }
          
            GL11.glEnd();
            
            GL11.glPopMatrix();
            break;
        }
        case POLYGON:
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(position.x, position.y, 0.0f);
            GL11.glRotatef(angle, 0.0f, 0.0f, 1.0f);
            
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, Texture.NO_TEXTURE);

            GL11.glColor3f(1.0f, 1.0f, 1.0f);
            GL11.glBegin(GL11.GL_POLYGON);

            for (int i = 0; i < vertices.length; i++)
               GL11.glVertex2f(vertices[i].x, vertices[i].y);
          
            GL11.glEnd();
            
            GL11.glPopMatrix();
            break;
        }
        }
    }
}
