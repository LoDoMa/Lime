package net.lodoma.lime.mask;

import net.lodoma.lime.gui.Color;
import net.lodoma.lime.mask.MaskShape.ShapeType;
import net.lodoma.lime.texture.Texture;
import net.lodoma.lime.util.Vector2;

import static org.lwjgl.opengl.GL11.*;

public class ColoredMask extends Mask
{
    public static final int CIRCLE_VERTEX_COUNT = 16;
    
    protected Vector2[] vertices;
    protected Color[] colors;
    
    public ColoredMask(MaskShape shape)
    {
        if(shape.getType() == ShapeType.POLYGON)
        {
            vertices = shape.getVertices();
            colors = shape.getColors();
        }
    }
    
    @Override
    public void render()
    {
        glBindTexture(GL_TEXTURE_2D, Texture.NO_TEXTURE);
        glBegin(GL_POLYGON);
        for(int i = 0; i < vertices.length; i++)
        {
            colors[i].setGL();
            glVertex2f(vertices[i].x, vertices[i].y);
        }
        glEnd();
    }
}
