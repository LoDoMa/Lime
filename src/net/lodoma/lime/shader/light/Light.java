package net.lodoma.lime.shader.light;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
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
        synchronized (FBO.destroyList)
        {
            if (occlusionLocal != null) FBO.destroyList.add(occlusionLocal);
            if (shadowMap != null) FBO.destroyList.add(shadowMap);
        }
    }
    
    public void renderBrightness(FBO brightnessMap)
    {
        brightnessMap.bind();
        
        Program.brightnessProgram.useProgram();
        
        glPushMatrix();
        Camera.scale();
        
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
    
    public void renderDSL(FBO occlusionMap, FBO lightMap)
    {
        float lightFW = Window.viewportWidth * data.radius / Camera.SCALEW;
        float lightFH = Window.viewportHeight * data.radius / Camera.SCALEH;
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
        Program.occlusionCopyProgram.setUniform("occlusionMap", UniformType.INT1, 0);

        float occlusionLX = (data.position.x - data.radius) / Camera.SCALEW;
        float occlusionHX = (data.position.x + data.radius) / Camera.SCALEW;
        float occlusionLY = (data.position.y - data.radius) / Camera.SCALEH;
        float occlusionHY = (data.position.y + data.radius) / Camera.SCALEH;
        
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
        Program.shadowMapProgram.setUniform("texture", UniformType.INT1, 0);
        Program.shadowMapProgram.setUniform("resolution", UniformType.FLOAT2, lightFW, lightFH);
        
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
        Program.renderLightProgram.setUniform("texture", UniformType.INT1, 0);
        Program.renderLightProgram.setUniform("resolution", UniformType.FLOAT2, lightFW, lightFH);
        
        glPushMatrix();
        Camera.scale();
        
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
