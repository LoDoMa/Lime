package net.lodoma.lime.world.gfx;

import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.shader.Program;
import net.lodoma.lime.shader.UniformType;
import net.lodoma.lime.shader.light.Light;
import net.lodoma.lime.world.World;
import net.lodoma.lime.world.entity.Entity;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.EXTFramebufferObject.*;

public class WorldRenderer
{
    private World world;

    private int viewportWidth = -1;
    private int viewportHeight = -1;

    private int[] lightFBO;
    private int[] worldFBO;
    
    public WorldRenderer(World world)
    {
        this.world = world;
    }
    
    private int generateTexture(int width, int height)
    {
        int texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_BYTE, (java.nio.ByteBuffer) null);
        
        return texID;
    }
    
    private int[] generateFramebuffer(int width, int height)
    {
        int fbo = glGenFramebuffersEXT();
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fbo);
        
        int ct = generateTexture(width, height);
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, ct, 0);
        
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
        
        return new int[] {fbo, ct, width, height};
    }
    
    private void destroyFramebuffer(int[] data)
    {
        glDeleteFramebuffersEXT(data[0]);
        glDeleteTextures(data[1]);
    }
    
    private void bindFramebuffer(int[] data)
    {
        glViewport(0, 0, data[2], data[3]);
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, data[0]);
    }
    
    public void clean()
    {
        if (lightFBO != null) destroyFramebuffer(lightFBO);
        if (worldFBO != null) destroyFramebuffer(worldFBO);
    }
    
    private void renderLights()
    {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT);
        glLoadIdentity();
        
        glPushMatrix();
        glScalef(1.0f / 32.0f, 1.0f / 24.0f, 1.0f);
        
        world.lightPool.foreach((Light light) -> light.render());
        
        glPopMatrix();
    }
    
    private void renderWorld()
    {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT);
        glLoadIdentity();
        
        glPushMatrix();
        glScalef(1.0f / 32.0f, 1.0f / 24.0f, 1.0f);
        
        Program.worldProgram.useProgram();
        world.entityPool.foreach((Entity entity) -> entity.render());
        
        glPopMatrix();
    }
    
    private void renderFull(int lightTexture, int worldTexture)
    {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT);
        glLoadIdentity();
        
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, lightTexture);
        
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, worldTexture);
        
        glActiveTexture(GL_TEXTURE0);

        Program.copyProgram.useProgram();
        Program.copyProgram.setUniform("light", UniformType.INT1, 0);
        Program.copyProgram.setUniform("world", UniformType.INT1, 1);
        
        glBegin(GL_QUADS);
        glTexCoord2f(0.0f, 0.0f); glVertex2f(0.0f, 0.0f);
        glTexCoord2f(1.0f, 0.0f); glVertex2f(1.0f, 0.0f);
        glTexCoord2f(1.0f, 1.0f); glVertex2f(1.0f, 1.0f);
        glTexCoord2f(0.0f, 1.0f); glVertex2f(0.0f, 1.0f);
        glEnd();
    }
    
    public void render()
    {
        if (Window.viewportWidth != viewportWidth || Window.viewportHeight != viewportHeight)
        {
            viewportWidth = Window.viewportWidth;
            viewportHeight = Window.viewportHeight;

            if (lightFBO != null) destroyFramebuffer(lightFBO);
            if (worldFBO != null) destroyFramebuffer(worldFBO);
            
            lightFBO = generateFramebuffer(viewportWidth, viewportHeight);
            worldFBO = generateFramebuffer(viewportWidth, viewportHeight);
        }

        bindFramebuffer(lightFBO);
        renderLights();
        
        bindFramebuffer(worldFBO);
        renderWorld();
        
        Window.bindFBO();
        renderFull(lightFBO[1], worldFBO[1]);
    }
}
