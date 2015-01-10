package net.lodoma.lime.world.gfx;

import java.io.File;

import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.shader.Program;
import net.lodoma.lime.shader.Shader;
import net.lodoma.lime.shader.ShaderType;
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

    private boolean initialized;
    
    private Program worldProgram;
    private Program copyProgram;

    private int fbow;
    private int fboh;

    private int[] lightFBO;
    private int[] worldFBO;
    
    public WorldRenderer(World world)
    {
        this.world = world;
        initialized = false;
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
    
    private void useFramebuffer(int[] data)
    {
        glViewport(0, 0, data[2], data[3]);
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, data[0]);
    }
    
    private void init()
    {
        // FIXME: framebuffers are always the same size size as the default viewport
        fbow = Window.viewportWidth;
        fboh = Window.viewportHeight;

        lightFBO = generateFramebuffer(fbow, fboh);
        worldFBO = generateFramebuffer(fbow, fboh);
        
        Shader worldVS = Shader.getShader("worldVS", new File("shader/world.vs"), ShaderType.VERTEX);
        Shader worldFS = Shader.getShader("worldFS", new File("shader/world.fs"), ShaderType.FRAGMENT);
        Shader copyVS = Shader.getShader("copyVS", new File("shader/copy.vs"), ShaderType.VERTEX);
        Shader copyFS = Shader.getShader("copyFS", new File("shader/copy.fs"), ShaderType.FRAGMENT);
        
        worldProgram = Program.getProgram("world", worldVS, worldFS);
        copyProgram = Program.getProgram("copy", copyVS, copyFS);
        
        initialized = true;
    }
    
    public void clean()
    {
        destroyFramebuffer(lightFBO);
        destroyFramebuffer(worldFBO);
        
        initialized = false;
    }
    
    private void renderLights()
    {
        useFramebuffer(lightFBO);
        
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT);
        glLoadIdentity();

        glPushMatrix();
        glScalef(1.0f / 32.0f, 1.0f / 24.0f, 1.0f);

        world.lightPool.foreach((Light light) ->
        {
            light.render();
        });
        
        glPopMatrix();
    }
    
    private void renderWorld()
    {
        // useFramebuffer(worldFBO);
        
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT);
        glLoadIdentity();
        
        glPushMatrix();
        glScalef(1.0f / 32.0f, 1.0f / 24.0f, 1.0f);
        
        worldProgram.useProgram();
        
        world.entityPool.foreach((Entity entity) -> {
            entity.render();
        });
        
        glPopMatrix();
    }
    
    private void renderDebug()
    {
        glPushMatrix();
        glScalef(1.0f / 32.0f, 1.0f / 24.0f, 1.0f);
        
        worldProgram.useProgram();
        
        world.entityPool.foreach((Entity entity) -> {
            entity.debugRender();
        });
        
        glPopMatrix();
    }
    
    public void render()
    {
        if(!initialized)
            init();
        
        renderLights();

        renderWorld();
        
        Window.bindFBO();
        
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT);
        glLoadIdentity();

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, lightFBO[1]);
        
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, worldFBO[1]);
        
        glActiveTexture(GL_TEXTURE0);

        copyProgram.useProgram();
        
        copyProgram.setUniform("light", UniformType.INT1, 0);
        copyProgram.setUniform("world", UniformType.INT1, 1);
        
        glBegin(GL_QUADS);
        {
            glTexCoord2f(0.0f, 0.0f); glVertex2f(0.0f, 0.0f);
            glTexCoord2f(1.0f, 0.0f); glVertex2f(1.0f, 0.0f);
            glTexCoord2f(1.0f, 1.0f); glVertex2f(1.0f, 1.0f);
            glTexCoord2f(0.0f, 1.0f); glVertex2f(0.0f, 1.0f);
        }
        glEnd();
        
        renderDebug();
    }
}
