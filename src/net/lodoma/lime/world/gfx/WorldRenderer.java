package net.lodoma.lime.world.gfx;

import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.shader.Program;
import net.lodoma.lime.shader.UniformType;
import net.lodoma.lime.shader.light.Light;
import net.lodoma.lime.texture.Texture;
import net.lodoma.lime.world.World;
import net.lodoma.lime.world.physics.PhysicsComponentSnapshot;
import net.lodoma.lime.world.physics.PhysicsParticle;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class WorldRenderer
{
    private World world;

    private int viewportWidth = -1;
    private int viewportHeight = -1;

    private FBO occlusionMap;
    private FBO brightnessMap;
    private FBO lightMap;
    
    public Camera camera;
    
    public WorldRenderer(World world)
    {
        this.world = world;
        camera = new Camera();
    }
    
    public void clean()
    {
        if (occlusionMap != null) occlusionMap.destroy();
        if (brightnessMap != null) brightnessMap.destroy();
        if (lightMap != null) lightMap.destroy();
    }
    
    private void renderOcclusionMap()
    {
        occlusionMap.bind();
        occlusionMap.clear();

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        camera.transform();
        camera.scale();

        Program.basicProgram.useProgram();
        Program.basicProgram.setUniform("uTexture", UniformType.INT1, 0);
        Texture.NO_TEXTURE.bind(0);
        
        synchronized (world.lock)
        {
            world.compoSnapshotPool.foreach((PhysicsComponentSnapshot compoSnapshot) -> compoSnapshot.debugRender());
            world.particleList.forEach((PhysicsParticle particle) -> particle.debugRender());
        }
        
        occlusionMap.unbind();
    }
    
    private void renderBrightnessMap()
    {
        brightnessMap.bind();
        brightnessMap.clear();

        glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        
        synchronized (world.lock)
        {
            world.lightPool.foreach((Light light) -> light.renderBrightness(brightnessMap, camera));
        }
        
        brightnessMap.unbind();
    }
    
    private void renderLightMap()
    {
        lightMap.bind();
        lightMap.clear();

        glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        
        synchronized (world.lock)
        {
            world.lightPool.foreach((Light light) -> light.renderDSL(occlusionMap, lightMap, camera));
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
        glBindTexture(GL_TEXTURE_2D, brightnessMap.textureID);
        
        glActiveTexture(GL_TEXTURE2);
        glBindTexture(GL_TEXTURE_2D, lightMap.textureID);
        
        glActiveTexture(GL_TEXTURE0);

        Program.copyProgram.useProgram();
        Program.copyProgram.setUniform("occlusion", UniformType.INT1, 0);
        Program.copyProgram.setUniform("brightness", UniformType.INT1, 1);
        Program.copyProgram.setUniform("light", UniformType.INT1, 2);
        
        glBegin(GL_QUADS);
        glTexCoord2f(0.0f, 0.0f); glVertex2f(0.0f, 0.0f);
        glTexCoord2f(1.0f, 0.0f); glVertex2f(1.0f, 0.0f);
        glTexCoord2f(1.0f, 1.0f); glVertex2f(1.0f, 1.0f);
        glTexCoord2f(0.0f, 1.0f); glVertex2f(0.0f, 1.0f);
        glEnd();
    }
    
    public void render()
    {
        synchronized (camera)
        {
            if (Window.viewportWidth != viewportWidth || Window.viewportHeight != viewportHeight)
            {
                viewportWidth = Window.viewportWidth;
                viewportHeight = Window.viewportHeight;
    
                clean();
    
                occlusionMap = new FBO(viewportWidth, viewportHeight);
                brightnessMap = new FBO(viewportWidth, viewportHeight);
                lightMap = new FBO(viewportWidth, viewportHeight);
            }
    
            renderOcclusionMap();
            renderBrightnessMap();
            renderLightMap();
            
            renderFinal();
        }
    }
}
