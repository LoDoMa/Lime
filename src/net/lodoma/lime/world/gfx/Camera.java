package net.lodoma.lime.world.gfx;

import static org.lwjgl.opengl.GL11.*;
import net.lodoma.lime.util.Vector2;

public class Camera
{
    public Vector2 translation = new Vector2(0.0f, 0.0f);
    public float rotation = 0.0f;
    public Vector2 scale = new Vector2(1.0f, 1.0f);
    
    public void scale()
    {
        glScalef(1.0f / scale.x, 1.0f / scale.y, 1.0f);
    }
    
    public void transform()
    {
        glTranslatef(scale.x * 0.5f, scale.y * 0.5f, 0.0f);
        glRotatef(rotation, 0.0f, 0.0f, 1.0f);
        glTranslatef(scale.x * -0.5f, scale.y * -0.5f, 0.0f);
        glTranslatef(-translation.x, -translation.y, 0.0f);
    }
}
