package net.lodoma.lime.world.gfx;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lwjgl.BufferUtils;

import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.resource.fbo.FBO;
import net.lodoma.lime.resource.texture.Texture;
import net.lodoma.lime.shader.Program;
import net.lodoma.lime.shader.UniformType;
import net.lodoma.lime.shader.light.Light;
import net.lodoma.lime.world.World;
import net.lodoma.lime.world.physics.PhysicsComponentSnapshot;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;

public class WorldRenderer
{
    private World world;

    private int viewportWidth = -1;
    private int viewportHeight = -1;

    private FBO occlusionMap;
    private FBO brightnessMap;
    private FBO lightMap;
    
    public Camera camera;
    
    private int[] vbos;
    private int[] vertexcs;
    private String[] vbotexs;
    
    public WorldRenderer(World world)
    {
        this.world = world;
        camera = new Camera();
    }
    
    public void clean()
    {
        if (occlusionMap != null)
            FBO.destroyFBO(occlusionMap);
        if (brightnessMap != null)
            FBO.destroyFBO(brightnessMap);
        if (lightMap != null)
            FBO.destroyFBO(lightMap);
    }
    
    private void createVBO()
    {
        List<Vertex> vertices = new ArrayList<Vertex>();
        world.compoSnapshotPool.foreach((PhysicsComponentSnapshot compoSnapshot) -> compoSnapshot.getVertices(vertices));
        
        Map<String, List<Vertex>> texVerts = new HashMap<String, List<Vertex>>();
        for (Vertex v : vertices)
        {
            List<Vertex> texVList = texVerts.get(v.texture);
            if (texVList == null)
            {
                texVList = new ArrayList<Vertex>();
                texVerts.put(v.texture, texVList);
            }
            texVList.add(v);
        }

        vbos = new int[texVerts.size()];
        vertexcs = new int[texVerts.size()];
        vbotexs = new String[texVerts.size()];
        
        int i = 0;
        Set<String> textures = texVerts.keySet();
        for (String texture : textures)
        {
            List<Vertex> texVList = texVerts.get(texture);
            
            FloatBuffer vbuff = BufferUtils.createFloatBuffer(texVList.size() * 8);
            for (Vertex v : texVList)
                vbuff.put(v.x).put(v.y).put(v.r).put(v.g).put(v.b).put(v.a).put(v.s).put(v.t);
            vbuff.flip();

            vbos[i] = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vbos[i]);
            glBufferData(GL_ARRAY_BUFFER, vbuff, GL_STATIC_DRAW);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            
            vertexcs[i] = texVList.size();

            if (texture != null)
                Texture.referenceUp(texture);
            vbotexs[i] = texture;
            
            i++;
        }
    }
    
    private void renderVBO()
    {
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        for (int i = 0; i < vbos.length; i++)
        {
            glBindBuffer(GL_ARRAY_BUFFER, vbos[i]);
            glVertexPointer(2, GL_FLOAT, Vertex.stride, Vertex.vertexOffset);
            glColorPointer(4, GL_FLOAT, Vertex.stride, Vertex.colorOffset);
            glTexCoordPointer(2, GL_FLOAT, Vertex.stride, Vertex.textureOffset);
            
            if (vbotexs[i] == null)
                Texture.NO_TEXTURE.bind(0);
            else
                Texture.get(vbotexs[i]).bind(0);
            glDrawArrays(GL_TRIANGLES, 0, vertexcs[i]);
        }
        
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_COLOR_ARRAY);
        glDisableClientState(GL_VERTEX_ARRAY);
    }
    
    private void destroyVBO()
    {
        for (int i = 0; i < vbos.length; i++)
        {
            glDeleteBuffers(vbos[i]);
            if (vbotexs[i] != null)
                Texture.referenceDown(vbotexs[i]);
        }
    }
    
    // TODO: optimize, render only things within this square
    public void renderOcclusion(float lowX, float lowY, float highX, float highY)
    {
        float scaleX = 1.0f / (highX - lowX);
        float scaleY = 1.0f / (highY - lowY);
        
        glPushMatrix();
        glScalef(scaleX, scaleY, 1.0f);
        glTranslatef(-lowX, -lowY, 0.0f);
        
        Program.basicProgram.useProgram();
        Program.basicProgram.setUniform("uTexture", UniformType.INT1, 0);
        Texture.NO_TEXTURE.bind(0);
        
        renderVBO();
        
        glPopMatrix();
    }
    
    private void renderOcclusionMap()
    {
        occlusionMap.bind();
        occlusionMap.clear();
        
        Program.basicProgram.useProgram();
        Program.basicProgram.setUniform("uTexture", UniformType.INT1, 0);
        Texture.NO_TEXTURE.bind(0);
        
        camera.transform();
        camera.scale();

        renderVBO();
        
        occlusionMap.unbind();
    }
    
    private void renderBrightnessMap()
    {
        brightnessMap.bind();
        brightnessMap.clear();
        
        Program.basicProgram.useProgram();
        Program.basicProgram.setUniform("uTexture", UniformType.INT1, 0);
        Texture.NO_TEXTURE.bind(0);

        world.lightAmbientColor.setGL();
        
        glBegin(GL_QUADS);
        glTexCoord2f(0.0f, 0.0f); glVertex2f(0.0f, 0.0f);
        glTexCoord2f(1.0f, 0.0f); glVertex2f(1.0f, 0.0f);
        glTexCoord2f(1.0f, 1.0f); glVertex2f(1.0f, 1.0f);
        glTexCoord2f(0.0f, 1.0f); glVertex2f(0.0f, 1.0f);
        glEnd();
        
        
        glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        
        world.lightPool.foreach((Light light) -> {
            if (light.inView(camera))
                light.renderBrightness(brightnessMap, camera);
        });
        
        brightnessMap.unbind();
    }
    
    private void renderLightMap()
    {
        lightMap.bind();
        lightMap.clear();
        
        Program.basicProgram.useProgram();
        Program.basicProgram.setUniform("uTexture", UniformType.INT1, 0);
        Texture.NO_TEXTURE.bind(0);

        world.lightAmbientColor.setGL();
        
        glBegin(GL_QUADS);
        glTexCoord2f(0.0f, 0.0f); glVertex2f(0.0f, 0.0f);
        glTexCoord2f(1.0f, 0.0f); glVertex2f(1.0f, 0.0f);
        glTexCoord2f(1.0f, 1.0f); glVertex2f(1.0f, 1.0f);
        glTexCoord2f(0.0f, 1.0f); glVertex2f(0.0f, 1.0f);
        glEnd();
        
        glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        
        world.lightPool.foreach((Light light) -> {
            if (light.inView(camera))
                light.renderDSL(this, lightMap, camera);
        });
        
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
    
    public void renderDebug()
    {
        Window.bindFBO();

        glPushMatrix();
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        camera.transform();
        camera.scale();
        
        Program.basicProgram.useProgram();
        Program.basicProgram.setUniform("uTexture", UniformType.INT1, 0);
        Texture.NO_TEXTURE.bind(0);

        world.lightPool.foreach((Light light) -> light.debugRender());
        world.compoSnapshotPool.foreach((PhysicsComponentSnapshot compoSnapshot) -> compoSnapshot.debugRender());
        glPopMatrix();
    }
    
    public void render()
    {
        synchronized (world.lock)
        {
            if (Window.viewportWidth != viewportWidth || Window.viewportHeight != viewportHeight)
            {
                viewportWidth = Window.viewportWidth;
                viewportHeight = Window.viewportHeight;
    
                clean();
    
                occlusionMap = FBO.newFBO(viewportWidth, viewportHeight);
                brightnessMap = FBO.newFBO(viewportWidth, viewportHeight);
                lightMap = FBO.newFBO(viewportWidth, viewportHeight);
            }
            
            createVBO();
            renderOcclusionMap();
            renderBrightnessMap();
            renderLightMap();
            destroyVBO();
            
            renderFinal();
            
            if (Window.debugEnabled)
                renderDebug();
        }
    }
}
