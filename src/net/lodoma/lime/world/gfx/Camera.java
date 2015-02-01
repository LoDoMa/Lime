package net.lodoma.lime.world.gfx;

import static org.lwjgl.opengl.GL11.*;

public class Camera
{
    public static final float SCALEW = 32.0f;
    public static final float SCALEH = 18.0f;
    
    public static void scale()
    {
        glScalef(1.0f / SCALEW, 1.0f / SCALEH, 1.0f);
    }
}
