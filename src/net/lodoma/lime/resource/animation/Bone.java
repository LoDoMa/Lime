package net.lodoma.lime.resource.animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.lodoma.lime.resource.texture.Texture;
import net.lodoma.lime.util.Vector2;
import static org.lwjgl.opengl.GL11.*;

public class Bone
{
    public List<Bone> childrenBack = new ArrayList<Bone>();
    public List<Bone> childrenFront = new ArrayList<Bone>();
    
    public Map<String, float[]> keyFrames = new HashMap<String, float[]>();
    public Map<String, float[]> frameDurations = new HashMap<String, float[]>();
    
    public Vector2 offset;
    
    private float crotation;
    private int cframe;

    public String textureName;
    public Vector2 textureSize;
    public Vector2 textureOffset;
    
    public void create()
    {
        if (textureName != null)
            Texture.referenceUp(textureName);

        for (Bone child : childrenBack) child.create();
        for (Bone child : childrenFront) child.create();
    }
    
    public void destroy()
    {
        for (Bone child : childrenBack) child.destroy();
        for (Bone child : childrenFront) child.destroy();
        
        if (textureName != null)
            Texture.referenceDown(textureName);
    }
    
    public void update(String animation, float time)
    {
        float[] frameDurationArray = frameDurations.get(animation);
        float[] keyFrameArray = keyFrames.get(animation);
        
        cframe = -1;
        float cdur = 0.0f;
        do { cdur += frameDurationArray[++cframe]; } while (cdur < time);
        
        crotation = keyFrameArray[cframe];
        crotation += (time - cdur + frameDurationArray[cframe]) / frameDurationArray[cframe] * (keyFrameArray[(cframe + 1) % keyFrameArray.length] - crotation);

        for (Bone child : childrenBack) child.update(animation, time);
        for (Bone child : childrenFront) child.update(animation, time);
    }
    
    public void render()
    {
        glPushMatrix();
        glTranslatef(offset.x, offset.y, 0.0f);
        glRotatef(-crotation, 0.0f, 0.0f, 1.0f);

        for (Bone child : childrenBack)
            child.render();
        
        if (textureName != null)
        {
            Texture.get(textureName).bind(0);
            
            glTranslatef(textureOffset.x, textureOffset.y, 0.0f);
            glBegin(GL_QUADS);
            glTexCoord2f(0.0f, 1.0f); glVertex2f(textureSize.x / -2.0f, textureSize.y / -2.0f);
            glTexCoord2f(1.0f, 1.0f); glVertex2f(textureSize.x / +2.0f, textureSize.y / -2.0f);
            glTexCoord2f(1.0f, 0.0f); glVertex2f(textureSize.x / +2.0f, textureSize.y / +2.0f);
            glTexCoord2f(0.0f, 0.0f); glVertex2f(textureSize.x / -2.0f, textureSize.y / +2.0f);
            glEnd();
            glTranslatef(-textureOffset.x, -textureOffset.y, 0.0f);
        }

        for (Bone child : childrenFront)
            child.render();
        
        glPopMatrix();
    }
}