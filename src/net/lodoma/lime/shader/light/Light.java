package net.lodoma.lime.shader.light;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.resource.fbo.FBO;
import net.lodoma.lime.resource.texture.Texture;
import net.lodoma.lime.shader.Program;
import net.lodoma.lime.shader.UniformType;
import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.world.World;
import net.lodoma.lime.world.gfx.Camera;
import net.lodoma.lime.world.gfx.WorldRenderer;

public class Light implements Identifiable<Integer>
{
    public int identifier;
    
    public World world;
    public LightData data;
    
    private static FBO occlusion;
    private static FBO shadowMap;
    
    public static void destroyFBOs()
    {
        if (occlusion != null)
            FBO.destroyFBO(occlusion);
        if (shadowMap != null)
            FBO.destroyFBO(shadowMap);
        occlusion = null;
        shadowMap = null;
    }
    
    public Light(World world)
    {
        this.world = world;
        data = new LightData();
    }
    
    @Override
    public Integer getIdentifier()
    {
        return identifier;
    }
    
    @Override
    public void setIdentifier(Integer identifier)
    {
        this.identifier = identifier;
    }
    
    public void destroy()
    {
        
    }
    
    public boolean inView(Camera camera)
    {
        if (camera.translation.x > (data.position.x + data.radius)) return false;
        if (camera.translation.y > (data.position.y + data.radius)) return false;
        if ((camera.translation.x + camera.scale.x) < (data.position.x - data.radius)) return false;
        if ((camera.translation.y + camera.scale.y) < (data.position.y - data.radius)) return false;
        return true;
    }
    
    public void renderBrightness(FBO brightnessMap, Camera camera)
    {
        brightnessMap.bind();
        
        Program.brightnessProgram.useProgram();
        
        glPushMatrix();
        camera.transform();
        camera.scale();
        
        data.color.setGL();
        
        glBegin(GL_QUADS);
        glTexCoord2f(0.0f, 1.0f); glVertex2f(data.position.x - data.radius, data.position.y - data.radius);
        glTexCoord2f(1.0f, 1.0f); glVertex2f(data.position.x + data.radius, data.position.y - data.radius);
        glTexCoord2f(1.0f, 0.0f); glVertex2f(data.position.x + data.radius, data.position.y + data.radius);
        glTexCoord2f(0.0f, 0.0f); glVertex2f(data.position.x - data.radius, data.position.y + data.radius);
        glEnd();
        
        glPopMatrix();
        
        brightnessMap.unbind();
    }
    
    public void renderDSL(WorldRenderer renderer, FBO lightMap, Camera camera)
    {
        float lightFW = Window.viewportWidth * data.radius / camera.scale.x;
        
        /*
         * Create/recreate FBOs if needed
         */
        
        if (occlusion == null)
        {
            occlusion = FBO.newFBO(512, 512);
            shadowMap = FBO.newFBO(512, 1);
            
            glBindTexture(GL_TEXTURE_2D, shadowMap.textureID);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        }
        
        /*
         * Render occlusion
         */
        
        occlusion.bind();
        occlusion.clear();

        float lightLX = data.position.x - data.radius;
        float lightLY = data.position.y - data.radius;
        float lightHX = data.position.x + data.radius;
        float lightHY = data.position.y + data.radius;
        renderer.renderOcclusion(lightLX, lightLY, lightHX, lightHY);
        
        occlusion.unbind();
        
        /*
         * Create a 1D shadow map
         */
        
        shadowMap.bind();
        shadowMap.clear();
        
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, occlusion.textureID);
        
        Program.shadowMapProgram.useProgram();
        Program.shadowMapProgram.setUniform("uTexture", UniformType.INT1, 0);
        Program.shadowMapProgram.setUniform("uResolution", UniformType.FLOAT1, lightFW);
        
        data.color.setGL();
        
        glBegin(GL_QUADS);
        glTexCoord2f(0.0f, 0.0f); glVertex2f(0.0f, 0.0f);
        glTexCoord2f(1.0f, 0.0f); glVertex2f(1.0f, 0.0f);
        glTexCoord2f(1.0f, 1.0f); glVertex2f(1.0f, 1.0f);
        glTexCoord2f(0.0f, 1.0f); glVertex2f(0.0f, 1.0f);
        glEnd();
        
        shadowMap.unbind();
        
        /*
         * Render the light to the light map FBO
         */
        
        lightMap.bind();

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, shadowMap.textureID);
        
        Program.renderLightProgram.useProgram();
        Program.renderLightProgram.setUniform("uTexture", UniformType.INT1, 0);
        Program.renderLightProgram.setUniform("uResolution", UniformType.FLOAT1, lightFW);
        
        glPushMatrix();
        camera.transform();
        camera.scale();
        
        glBegin(GL_QUADS);
        glTexCoord2f(0.0f, 1.0f); glVertex2f(data.position.x - data.radius, data.position.y - data.radius);
        glTexCoord2f(1.0f, 1.0f); glVertex2f(data.position.x + data.radius, data.position.y - data.radius);
        glTexCoord2f(1.0f, 0.0f); glVertex2f(data.position.x + data.radius, data.position.y + data.radius);
        glTexCoord2f(0.0f, 0.0f); glVertex2f(data.position.x - data.radius, data.position.y + data.radius);
        glEnd();
        
        glPopMatrix();
        
        lightMap.unbind();
    }
    
    public void debugRender()
    {
        glPushMatrix();
        glTranslatef(data.position.x, data.position.y, 0.0f);
        
        Texture.NO_TEXTURE.bind(0);
        glColor3f(1.0f, 1.0f, 1.0f);
        glBegin(GL_LINES);
        for (int i = 0; i <= 10; i++)
        {
            float angle = (float) Math.toRadians(i * 360.0 / 10.0);
            float x = (float) Math.cos(angle);
            float y = (float) Math.sin(angle);
            glVertex2f(0.0f, 0.0f);
            glVertex2f(x * 0.25f, y * 0.25f);
        }
        glEnd();
        
        glPopMatrix();
    }
}
