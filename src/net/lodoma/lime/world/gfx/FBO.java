package net.lodoma.lime.world.gfx;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.EXTFramebufferObject.*;

import java.util.ArrayList;
import java.util.List;

import net.lodoma.lime.client.window.Window;

public class FBO
{
    public static List<FBO> destroyList = new ArrayList<FBO>();
    
    public int textureID;
    public int fboID;
    public int width;
    public int height;
    
    public FBO(int width, int height)
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
    
    public void clear()
    {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT);
        glLoadIdentity();
    }
    
    public void destroy()
    {
        glDeleteFramebuffersEXT(fboID);
        glDeleteTextures(textureID);
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
