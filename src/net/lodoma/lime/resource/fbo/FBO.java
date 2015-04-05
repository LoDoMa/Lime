package net.lodoma.lime.resource.fbo;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.EXTFramebufferObject.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.lodoma.lime.Lime;
import net.lodoma.lime.client.window.Window;

public class FBO
{
    private static Object lock = new Object();
    private static Set<FBO> fbos = new HashSet<FBO>();
    private static List<FBO> destroyList = new ArrayList<FBO>();
    
    public static FBO newFBO(int width, int height)
    {
        synchronized (lock)
        {
            FBO fbo = new FBO(width, height);
            fbos.add(fbo);
            Lime.LOGGER.D("Created FBO " + fbo.fboID);
            return fbo;
        }
    }
    
    public static void destroyFBO(FBO fbo)
    {
        synchronized (lock)
        {
            destroyList.add(fbo);
            fbos.remove(fbo);
        }
    }
    
    public static void updateAll()
    {
        synchronized (lock)
        {
            for (FBO fbo : destroyList)
            {
                fbo.destroy();
                Lime.LOGGER.D("Destroyed FBO " + fbo.fboID);
            }
            destroyList.clear();
        }
    }
    
    public static void forceDeleteAll()
    {
        synchronized (lock)
        {
            destroyList.addAll(fbos);
            fbos.clear();
        }
    }
    
    public final int textureID;
    public final int fboID;
    public final int width;
    public final int height;
    
    private FBO(int width, int height)
    {
        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_BYTE, (java.nio.ByteBuffer) null);

        fboID = glGenFramebuffersEXT();
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fboID);
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, textureID, 0);
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
        
        this.width = width;
        this.height = height;
    }
    
    private void destroy()
    {
        glDeleteFramebuffersEXT(fboID);
        glDeleteTextures(textureID);
    }
    
    public void clear()
    {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT);
        glLoadIdentity();
    }
    
    public void bind()
    {
        glViewport(0, 0, width, height);
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fboID);
    }
    
    public void unbind()
    {
        glViewport(Window.viewportX, Window.viewportY, Window.viewportWidth, Window.viewportHeight);
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
    }
}
