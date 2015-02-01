package net.lodoma.lime.world.gfx;

import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.shader.Program;
import net.lodoma.lime.shader.UniformType;
import net.lodoma.lime.shader.light.Light;
import net.lodoma.lime.world.World;
import net.lodoma.lime.world.physics.PhysicsComponentSnapshot;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class WorldRenderer
{
    private World world;

    private int viewportWidth = -1;
    private int viewportHeight = -1;

    private FBO occlusionMap;
    private FBO lightMap;
    
    public WorldRenderer(World world)
    {
        this.world = world;
    }
    
    public void clean()
    {
        if (occlusionMap != null) occlusionMap.destroy();
        if (lightMap != null) lightMap.destroy();
    }
    
    private void renderOcclusionMap()
    {
        occlusionMap.bind();
        occlusionMap.clear();

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glScalef(1.0f / 32.0f, 1.0f / 24.0f, 1.0f);
        
        Program.worldProgram.useProgram();
        synchronized (world.lock)
        {
            world.compoSnapshotPool.foreach((PhysicsComponentSnapshot compoSnapshot) -> compoSnapshot.debugRender());
        }
        
        occlusionMap.unbind();
    }
    
    private void renderLightMap()
    {
        lightMap.bind();
        lightMap.clear();

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        synchronized (world.lock)
        {
            world.lightPool.foreach((Light light) -> light.renderDSL(occlusionMap, lightMap));
        }
        
        lightMap.unbind();
    }
    
    private void renderFinal()
    {
        Window.bindFBO();
        
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT);
        glLoadIdentity();
        
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, occlusionMap.textureID);
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, lightMap.textureID);
        
        glActiveTexture(GL_TEXTURE0);

        Program.copyProgram.useProgram();
        Program.copyProgram.setUniform("light", UniformType.INT1, 1);
        Program.copyProgram.setUniform("world", UniformType.INT1, 0);
        
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

            clean();

            occlusionMap = new FBO(viewportWidth, viewportHeight);
            lightMap = new FBO(viewportWidth, viewportHeight);
        }

        renderOcclusionMap();
        renderLightMap();
        
        renderFinal();
    }
}
