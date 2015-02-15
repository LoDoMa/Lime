package net.lodoma.lime.shader.light;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import net.lodoma.lime.Lime;
import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.shader.Program;
import net.lodoma.lime.shader.UniformType;
import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.world.World;
import net.lodoma.lime.world.gfx.Camera;
import net.lodoma.lime.world.gfx.FBO;

public class Light implements Identifiable<Integer>
{
    public int identifier;
    
    public World world;
    public LightData data;
    
    private FBO occlusionLocal;
    private FBO shadowMap;
    
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
        if (occlusionLocal != null || shadowMap != null)
            Lime.LOGGER.I("Added light " + identifier + " FBOs to the destruction list");
        
        synchronized (FBO.destroyList)
        {
            if (occlusionLocal != null) FBO.destroyList.add(occlusionLocal);
            if (shadowMap != null) FBO.destroyList.add(shadowMap);
        }
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
    
    public void renderDSL(FBO occlusionMap, FBO lightMap, Camera camera)
    {
        float lightFW = Window.viewportWidth * data.radius / camera.scale.x;
        float lightFH = Window.viewportHeight * data.radius / camera.scale.y;
        int lightRW = (int) lightFW;
        int lightRH = (int) lightFH;
        
        if (occlusionLocal == null || occlusionLocal.width != lightRW || occlusionLocal.height != lightRH)
        {
            if (occlusionLocal != null)
                occlusionLocal.destroy();
            occlusionLocal = new FBO(lightRW, lightRH);
        }
        
        if (shadowMap == null || shadowMap.width != lightRW)
        {
            if (shadowMap != null)
                shadowMap.destroy();
            shadowMap = new FBO(lightRW, 1);

            glBindTexture(GL_TEXTURE_2D, shadowMap.textureID);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        }
        
        /*
         * Copy a section from the occlusion map FBO to the local occlusion FBO
         * NOTE: this can probably be skipped
         */
        
        occlusionLocal.bind();
        occlusionLocal.clear();

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, occlusionMap.textureID);
        
        Program.occlusionCopyProgram.useProgram();
        Program.occlusionCopyProgram.setUniform("uOcclusionMap", UniformType.INT1, 0);
        
        float occlusionLX = (data.position.x - data.radius - camera.translation.x) / camera.scale.x;
        float occlusionHX = (data.position.x + data.radius - camera.translation.x) / camera.scale.x;
        float occlusionLY = (data.position.y - data.radius - camera.translation.y) / camera.scale.y;
        float occlusionHY = (data.position.y + data.radius - camera.translation.y) / camera.scale.y;
        
        glBegin(GL_QUADS);
        glTexCoord2f(occlusionLX, occlusionLY); glVertex2f(0.0f, 0.0f);
        glTexCoord2f(occlusionHX, occlusionLY); glVertex2f(1.0f, 0.0f);
        glTexCoord2f(occlusionHX, occlusionHY); glVertex2f(1.0f, 1.0f);
        glTexCoord2f(occlusionLX, occlusionHY); glVertex2f(0.0f, 1.0f);
        glEnd();
        occlusionLocal.unbind();

        /*
         * Create a 1D shadow map
         */
        
        shadowMap.bind();
        shadowMap.clear();
        
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, occlusionLocal.textureID);
        
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
}
