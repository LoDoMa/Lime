package net.lodoma.lime.gui.exp;

import static org.lwjgl.opengl.GL11.*;
import net.lodoma.lime.util.Vector2;

public class GRectangle extends GShape
{
    protected final GDimension dimension = new GDimension();
    
    public GDimension getDimension()
    {
        return dimension;
    }
    
    @Override
    public boolean isPointInside(float x, float y)
    {
        Vector2 translation = getTranslation();
        float x2 = x - translation.x;
        float y2 = y - translation.y;
        float w = dimension.getWidth();
        float h = dimension.getHeight();
        return x2 >= 0 && x2 < w && y2 >= 0 && y2 < h;
    }
    
    @Override
    public void render()
    {
        getColor().setGL();
        
        glPushMatrix();
        {
            Vector2 translation = getTranslation();
            glTranslatef(translation.x, translation.y, 0.0f);
            glRotatef(getRotation(), 0.0f, 0.0f, 1.0f);

            float width = dimension.getWidth();
            float height = dimension.getHeight();
            
            glBegin(GL_QUADS);
            {
                glTexCoord2f(0.0f, 0.0f); glVertex2f(0.0f, 0.0f);
                glTexCoord2f(1.0f, 0.0f); glVertex2f(width, 0.0f);
                glTexCoord2f(1.0f, 1.0f); glVertex2f(width, height);
                glTexCoord2f(0.0f, 1.0f); glVertex2f(0.0f, height);
            }
            glEnd();
        }
        glPopMatrix();
    }
}
