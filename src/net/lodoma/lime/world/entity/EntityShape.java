package net.lodoma.lime.world.entity;

import org.lwjgl.opengl.GL11;

import net.lodoma.lime.texture.Texture;
import net.lodoma.lime.util.Vector2;

public class EntityShape
{
    public Vector2[] positionList = new Vector2[0];
    public float[] angleList = new float[0];
    public float[] radiusList = new float[0];

    public void debugRender()
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, Texture.NO_TEXTURE);
        
        for (int i = 0; i < positionList.length; i++)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(positionList[i].x, positionList[i].y, 0.0f);
            GL11.glRotatef(angleList[i], 0.0f, 0.0f, 1.0f);
            
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, Texture.NO_TEXTURE);
            
            GL11.glColor3f(1.0f, 0.0f, 0.0f);
            GL11.glBegin(GL11.GL_LINE_LOOP);
    
            for (int j = 0; j <= 10; j++)
            {
               float angle = (float) Math.toRadians(j * 360.0 / 10.0);
               GL11.glVertex2f((float) Math.cos(angle) * radiusList[i], (float) Math.sin(angle) * radiusList[i]);
            }
          
            GL11.glEnd();
            
            GL11.glPopMatrix();
        }
    }

    public void tempRender()
    {
        for (int i = 0; i < positionList.length; i++)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(positionList[i].x, positionList[i].y, 0.0f);
            GL11.glRotatef(angleList[i], 0.0f, 0.0f, 1.0f);
            
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, Texture.NO_TEXTURE);

            GL11.glColor3f(1.0f, 1.0f, 1.0f);
            GL11.glBegin(GL11.GL_TRIANGLE_FAN);

            GL11.glVertex2f(0.0f, 0.0f);
            for (int j = 0; j <= 10; j++)
            {
               float angle = (float) Math.toRadians(j * 360.0 / 10.0);
               GL11.glVertex2f((float) Math.cos(angle) * radiusList[i], (float) Math.sin(angle) * radiusList[i]);
            }
          
            GL11.glEnd();
            
            GL11.glPopMatrix();
        }
    }
}
