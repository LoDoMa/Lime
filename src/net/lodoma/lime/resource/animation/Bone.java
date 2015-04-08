package net.lodoma.lime.resource.animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.lodoma.lime.resource.texture.Texture;
import net.lodoma.lime.util.Vector2;
import net.lodoma.lime.world.gfx.Vertex;

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
    
    public void getVertices(List<Vertex> verts)
    {
        List<Vertex> verts2 = new ArrayList<Vertex>();

        for (Bone child : childrenBack)
            child.getVertices(verts2);
        
        if (textureName != null)
        {
            verts2.add(new Vertex().setXY(textureOffset.x + textureSize.x / -2.0f, textureOffset.y + textureSize.y / -2.0f).setRGBA(1.0f, 1.0f, 1.0f, 1.0f).setST(0.0f, 1.0f).setTexture(textureName));
            verts2.add(new Vertex().setXY(textureOffset.x + textureSize.x / +2.0f, textureOffset.y + textureSize.y / -2.0f).setRGBA(1.0f, 1.0f, 1.0f, 1.0f).setST(1.0f, 1.0f).setTexture(textureName));
            verts2.add(new Vertex().setXY(textureOffset.x + textureSize.x / +2.0f, textureOffset.y + textureSize.y / +2.0f).setRGBA(1.0f, 1.0f, 1.0f, 1.0f).setST(1.0f, 0.0f).setTexture(textureName));
            verts2.add(new Vertex().setXY(textureOffset.x + textureSize.x / -2.0f, textureOffset.y + textureSize.y / -2.0f).setRGBA(1.0f, 1.0f, 1.0f, 1.0f).setST(0.0f, 1.0f).setTexture(textureName));
            verts2.add(new Vertex().setXY(textureOffset.x + textureSize.x / +2.0f, textureOffset.y + textureSize.y / +2.0f).setRGBA(1.0f, 1.0f, 1.0f, 1.0f).setST(1.0f, 0.0f).setTexture(textureName));
            verts2.add(new Vertex().setXY(textureOffset.x + textureSize.x / -2.0f, textureOffset.y + textureSize.y / +2.0f).setRGBA(1.0f, 1.0f, 1.0f, 1.0f).setST(0.0f, 0.0f).setTexture(textureName));
        }
        
        for (Bone child : childrenFront)
            child.getVertices(verts2);
        
        for (Vertex v : verts2)
        {
            Vector2 rv = new Vector2(v.x, v.y).rotateDeg(-crotation);
            v.x = rv.x + offset.x;
            v.y = rv.y + offset.y;
        }
        verts.addAll(verts2);
    }
}
