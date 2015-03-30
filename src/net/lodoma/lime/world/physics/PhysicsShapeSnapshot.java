package net.lodoma.lime.world.physics;

import net.lodoma.lime.texture.Texture;
import net.lodoma.lime.util.Vector2;
import static org.lwjgl.opengl.GL11.*;

public class PhysicsShapeSnapshot
{
    public PhysicsShapeType shapeType;
    public float radius;
    public Vector2[] vertices;
    
    public PhysicsShape toShape()
    {
        switch (shapeType)
        {
        case CIRCLE:
        {
            PhysicsShapeCircle shape = new PhysicsShapeCircle();
            shape.radius = radius;
            return shape;
        }
        case POLYGON: case TRIANGLE_GROUP:
            PhysicsShapePolygon shape = new PhysicsShapePolygon();
            shape.vertices = vertices;
            return shape;
        default:
            throw new IllegalStateException();
        }
    }
    
    public void debugRender()
    {
        switch (shapeType)
        {
        case CIRCLE:
        {
            glScalef(radius, radius, 1.0f);

            Texture.NO_TEXTURE.bind();

            glColor3f(0.7f, 0.7f, 0.7f);
            glBegin(GL_TRIANGLE_FAN);

            glVertex2f(0.0f, 0.0f);
            for (int i = 0; i <= 10; i++)
            {
                float angle = (float) Math.toRadians(i * 360.0 / 10.0);
                glVertex2f((float) Math.cos(angle), (float) Math.sin(angle));
            }
          
            glEnd();

            glColor3f(0.3f, 0.3f, 0.3f);
            glBegin(GL_LINES);

            for (int i = 0; i <= 10; i++)
            {
                float angle = (float) Math.toRadians(i * 360.0 / 10.0);
                glVertex2f((float) Math.cos(angle), (float) Math.sin(angle));
                float angle2 = (float) Math.toRadians((i + 1) * 360.0 / 10.0);
                glVertex2f((float) Math.cos(angle2), (float) Math.sin(angle2));
            }
          
            glEnd();

            glScalef(1.0f / radius, 1.0f / radius, 1.0f);
            
            break;
        }
        case POLYGON:
        {
            Texture.NO_TEXTURE.bind();

            glColor3f(0.7f, 0.7f, 0.7f);
            glBegin(GL_POLYGON);

            for (int i = 0; i < vertices.length; i++)
                glVertex2f(vertices[i].x, vertices[i].y);
          
            glEnd();

            glColor3f(0.3f, 0.3f, 0.3f);
            glBegin(GL_LINES);

            for (int i = 0; i < vertices.length; i++)
            {
                glVertex2f(vertices[i].x, vertices[i].y);
                glVertex2f(vertices[(i + 1) % vertices.length].x, vertices[(i + 1) % vertices.length].y);
            }
          
            glEnd();
            glLineWidth(1.0f);
            
            break;
        }
        default:
            throw new IllegalStateException();
        }
    }
}
