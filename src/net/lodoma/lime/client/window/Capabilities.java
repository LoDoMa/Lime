package net.lodoma.lime.client.window;

import org.lwjgl.opengl.GLContext;

public class Capabilities
{
    private static boolean FBO;
    
    static boolean loaded = false;
    
    static void load()
    {
        FBO = GLContext.getCapabilities().GL_EXT_framebuffer_object;
        loaded = true;
    }
    
    static boolean checkRequired()
    {
        if(!FBO) return false;
        return true;
    }
    
    public static boolean supportsFBO()
    {
        return FBO;
    }
}
