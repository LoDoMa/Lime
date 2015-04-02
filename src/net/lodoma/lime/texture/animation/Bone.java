package net.lodoma.lime.texture.animation;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Bone
{
    public List<Bone> children = new ArrayList<Bone>();
    
    public float keyFrames[];
    public float frameDurations[];
    
    public float length;
    public float width;
    
    private float crotation;
    private int cframe;
    
    public void update(float time)
    { 
        cframe = -1;
        float cdur = 0.0f;
        do { cdur += frameDurations[++cframe]; } while (cdur < time);
        
        crotation = keyFrames[cframe];
        crotation += (time - cdur + frameDurations[cframe]) / frameDurations[cframe] * (keyFrames[(cframe + 1) % keyFrames.length] - crotation);
        
        for (Bone child : children)
            child.update(time);
    }
    
    public void render()
    {
        glPushMatrix();
        glRotatef(-crotation, 0.0f, 0.0f, 1.0f);

        glBegin(GL_QUADS);
        glVertex2f(-width / 2, 0.0f);
        glVertex2f( width / 2, 0.0f);
        glVertex2f( width / 2, length);
        glVertex2f(-width / 2, length);
        glEnd();
        
        glTranslatef(0.0f, length, 0.0f);
        for (Bone child : children)
            child.render();
        
        glPopMatrix();
    }
}
