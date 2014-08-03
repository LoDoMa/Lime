package net.lodoma.lime.world.client;

import java.io.File;
import java.util.List;

import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.physics.entity.Entity;
import net.lodoma.lime.shader.Program;
import net.lodoma.lime.shader.Shader;
import net.lodoma.lime.shader.ShaderPool;
import net.lodoma.lime.shader.ShaderType;
import net.lodoma.lime.shader.UniformType;
import net.lodoma.lime.world.platform.Platform;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.EXTFramebufferObject.*;

public class WorldRenderer
{
    private ClientsideWorld world;
    
    private boolean initialized;
    
    private ShaderPool pool;
    private Program lightProgram;
    private Program worldProgram;
    private Program copyProgram;

    private int fbow;
    private int fboh;

    private int[] lightFBO;
    private int[] worldFBO;
    
    public WorldRenderer(ClientsideWorld world)
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
    
    private void useFramebuffer(int[] data)
    {
        glViewport(0, 0, data[2], data[3]);
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, data[0]);
        
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT);
        glLoadIdentity();
    }
    
    private void init()
    {
        fbow = Window.getViewportWidth();
        fboh = Window.getViewportHeight();

        lightFBO = generateFramebuffer(fbow, fboh);
        worldFBO = generateFramebuffer(fbow, fboh);
        
        pool = new ShaderPool();
        pool.addShader("light.vs", new Shader(new File("shader/light.vs"), ShaderType.VERTEX));
        pool.addShader("light.fs", new Shader(new File("shader/light.fs"), ShaderType.FRAGMENT));
        pool.addShader("world.vs", new Shader(new File("shader/world.vs"), ShaderType.VERTEX));
        pool.addShader("world.fs", new Shader(new File("shader/world.fs"), ShaderType.FRAGMENT));
        pool.addShader("copy.vs", new Shader(new File("shader/copy.vs"), ShaderType.VERTEX));
        pool.addShader("copy.fs", new Shader(new File("shader/copy.fs"), ShaderType.FRAGMENT));
        
        lightProgram = new Program(pool, "light.vs", "light.fs");
        worldProgram = new Program(pool, "world.vs", "world.fs");
        copyProgram = new Program(pool, "copy.vs", "copy.fs");
        
        initialized = true;
    }
    
    public void renderLight(float x, float y, float r, float cr, float cg, float cb)
    {
        lightProgram.setUniform("lightColor", UniformType.FLOAT4, cr, cg, cb, 1.0f);
        lightProgram.setUniform("lightPos", UniformType.FLOAT2, x, y);
        lightProgram.setUniform("lightRadius", UniformType.FLOAT1, r);
        
        glBegin(GL_QUADS);
        {
            glVertex2f(x - r, y - r);
            glVertex2f(x + r, y - r);
            glVertex2f(x + r, y + r);
            glVertex2f(x - r, y + r);
        }
        glEnd();
    }
    
    private void renderLights()
    {
        useFramebuffer(lightFBO);

        glPushMatrix();
        glScalef(1.0f / 32.0f, 1.0f / 24.0f, 1.0f);
        
        lightProgram.useProgram();
        
        renderLight(16, 60, 80, 1.0f, 1.0f, 1.0f);
        renderLight(20, 9, 15, 1.0f, 0.0f, 0.0f);
        renderLight(0, 0, 20, 1.0f, 1.0f, 0.0f);
        renderLight(16, 7, 9, 0.5f, 0.5f, 1.0f);
        
        glPopMatrix();
    }
    
    private void renderWorld()
    {
        useFramebuffer(worldFBO);
        
        glPushMatrix();
        glScalef(1.0f / 32.0f, 1.0f / 24.0f, 1.0f);
        
        worldProgram.useProgram();
        
        List<Platform> platformList = world.getPlatformList();
        for(Platform platform : platformList)
            platform.render();
        
        List<Entity> entityList = world.getEntityList();
        for(Entity entity : entityList)
            entity.render();
        
        glPopMatrix();
    }
    
    public void render()
    {
        if(!initialized)
            init();
        
        
        glActiveTexture(GL_TEXTURE0);
        
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
    }
}
