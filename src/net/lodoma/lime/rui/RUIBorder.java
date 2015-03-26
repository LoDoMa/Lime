package net.lodoma.lime.rui;

import org.lwjgl.opengl.GL11;

import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.util.Color;

public class RUIBorder
{
    public float width;
    public float radius;
    
    public final Color color = new Color();
    
    public void renderBorder(RUIElement element)
    {
        float radiusx = radius * element.dimensions_c.x;
        float radiusy = radius * element.dimensions_c.y;
        if (radius < 0)
        {
            radiusx = radius / -Window.viewportWidth;
            radiusy = radius / -Window.viewportHeight;
        }

        GL11.glLineWidth(width);
        GL11.glBegin(GL11.GL_LINES);
        
        color.setGL();
        
        float firstx, firsty;
        firstx = firsty = 0;
        
        for (int i = 0; i <= 10; i++)
        {
            float angle = (float) Math.toRadians(90 - i * 90.0 / 10.0);
            float x = radiusx + (float) Math.cos(angle) * -radiusx;
            float y = radiusy + (float) Math.sin(angle) * -radiusy;
            if (i == 0)
            {
                firstx = x;
                firsty = y;
            }
            else
                GL11.glVertex2f(x, y);
            GL11.glVertex2f(x, y);
        }

        for (int i = 0; i <= 10; i++)
        {
            float angle = (float) Math.toRadians(i * 90.0 / 10.0);
            float x = radiusx + (float) Math.cos(angle) * -radiusx;
            float y = element.dimensions_c.y - radiusy + (float) Math.sin(angle) * radiusy;
            GL11.glVertex2f(x, y);
            GL11.glVertex2f(x, y);
        }

        for (int i = 0; i <= 10; i++)
        {
            float angle = (float) Math.toRadians(90 - i * 90.0 / 10.0);
            float x = element.dimensions_c.x - radiusx + (float) Math.cos(angle) * radiusx;
            float y = element.dimensions_c.y - radiusy + (float) Math.sin(angle) * radiusy;
            GL11.glVertex2f(x, y);
            GL11.glVertex2f(x, y);
        }

        for (int i = 0; i <= 10; i++)
        {
            float angle = (float) Math.toRadians(i * 90.0 / 10.0);
            float x = element.dimensions_c.x - radiusx + (float) Math.cos(angle) * radiusx;
            float y = radiusy + (float) Math.sin(angle) * -radiusy;
            GL11.glVertex2f(x, y);
            GL11.glVertex2f(x, y);
        }

        GL11.glVertex2f(firstx, firsty);
        
        GL11.glEnd();
    }
    
    public void fillBackground(RUIElement element)
    {
        float radiusx = radius * element.dimensions_c.x;
        float radiusy = radius * element.dimensions_c.y;
        if (radius < 0)
        {
            radiusx = radius / -Window.viewportWidth;
            radiusy = radius / -Window.viewportHeight;
        }
        
        GL11.glBegin(GL11.GL_POLYGON);
        
        for (int i = 0; i <= 10; i++)
        {
            float angle = (float) Math.toRadians(90 - i * 90.0 / 10.0);
            float x = radiusx + (float) Math.cos(angle) * -radiusx;
            float y = radiusy + (float) Math.sin(angle) * -radiusy;
            GL11.glVertex2f(x, y);
        }

        for (int i = 0; i <= 10; i++)
        {
            float angle = (float) Math.toRadians(i * 90.0 / 10.0);
            float x = radiusx + (float) Math.cos(angle) * -radiusx;
            float y = element.dimensions_c.y - radiusy + (float) Math.sin(angle) * radiusy;
            GL11.glVertex2f(x, y);
        }

        for (int i = 0; i <= 10; i++)
        {
            float angle = (float) Math.toRadians(90 - i * 90.0 / 10.0);
            float x = element.dimensions_c.x - radiusx + (float) Math.cos(angle) * radiusx;
            float y = element.dimensions_c.y - radiusy + (float) Math.sin(angle) * radiusy;
            GL11.glVertex2f(x, y);
        }

        for (int i = 0; i <= 10; i++)
        {
            float angle = (float) Math.toRadians(i * 90.0 / 10.0);
            float x = element.dimensions_c.x - radiusx + (float) Math.cos(angle) * radiusx;
            float y = radiusy + (float) Math.sin(angle) * -radiusy;
            GL11.glVertex2f(x, y);
        }
        
        GL11.glEnd();
    }
}
