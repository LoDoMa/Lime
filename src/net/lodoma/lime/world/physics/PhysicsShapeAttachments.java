package net.lodoma.lime.world.physics;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.lodoma.lime.Lime;
import net.lodoma.lime.texture.animation.Animation;
import net.lodoma.lime.texture.animation.AnimationLoader;
import net.lodoma.lime.texture.animation.AnimationPool;
import net.lodoma.lime.util.Color;
import net.lodoma.lime.util.Vector2;

// Shape attachments - data that stays with physical shapes after creation
public class PhysicsShapeAttachments
{
    public String name;
    
    public final Color color = new Color();
    public Animation animation;
    public String animationName;
    public final Vector2 animationRoot = new Vector2(0.0f);
    public final Vector2 animationScale = new Vector2(1.0f);
    public String textureName;
    public final Vector2 texturePoint = new Vector2(Float.NaN);
    public final Vector2 textureSize = new Vector2(Float.NaN);
    
    public boolean compareVisual(PhysicsShapeAttachments other)
    {
        if (!color.equals(other.color)) return false;

        if (!compareStringsWithNull(animationName, other.animationName)) return false;
        if (!Vector2.equals(animationRoot, other.animationRoot)) return false;
        if (!Vector2.equals(animationScale, other.animationScale)) return false;
        if (!compareStringsWithNull(textureName, other.textureName)) return false;
        if (!compareVectorsWithNaN(texturePoint, other.texturePoint)) return false;
        if (!compareVectorsWithNaN(textureSize, other.textureSize)) return false;
        
        return true;
    }
    
    public void infer(PhysicsShapeSnapshot shape)
    {
        if (textureName == null)
            // Nothing to infer if there is no texture
            return;
        
        float minX, minY, maxX, maxY;
        
        switch (shape.shapeType)
        {
        case CIRCLE:
            minX = minY = -shape.radius;
            maxX = maxY = shape.radius;
            break;
        case POLYGON:
            minX = maxX = shape.vertices[0].x;
            minY = maxY = shape.vertices[0].y;
            for (int i = 1; i < shape.vertices.length; i++)
            {
                if (shape.vertices[i].x < minX) minX = shape.vertices[i].x;
                if (shape.vertices[i].x > maxX) maxX = shape.vertices[i].x;
                if (shape.vertices[i].y < minY) minY = shape.vertices[i].y;
                if (shape.vertices[i].y > maxY) maxY = shape.vertices[i].y;
            }
            break;
        default:
            throw new IllegalStateException();
        }

        if (Float.isNaN(texturePoint.x)) texturePoint.x = minX;
        if (Float.isNaN(texturePoint.y)) texturePoint.y = minY;
        if (Float.isNaN(textureSize.x)) textureSize.x = maxX - minX;
        if (Float.isNaN(textureSize.y)) textureSize.y = maxY - minY;
    }

    public void readVisual(DataInputStream in) throws IOException
    {
        color.r = in.readFloat();
        color.g = in.readFloat();
        color.b = in.readFloat();
        color.a = in.readFloat();

        String oldAnimationName = animationName;
        if (in.readByte() == 1)
            animationName = in.readUTF();
        if (!compareStringsWithNull(oldAnimationName, animationName))
        {
            if (animation != null)
            {
                AnimationPool.remove(animation);
                animation.delete();
            }
            
            try
            {
                animation = AnimationLoader.load(animationName);
                animation.start();
            }
            catch(IOException e)
            {
                Lime.LOGGER.C("Failed to load animation " + animationName);
                Lime.LOGGER.log(e);
                Lime.forceExit(e);
            }
            
            AnimationPool.add(animation);
        }
        
        animationRoot.x = in.readFloat();
        animationRoot.y = in.readFloat();
        animationScale.x = in.readFloat();
        animationScale.y = in.readFloat();
        
        if (in.readByte() == 1)
            textureName = in.readUTF();

        texturePoint.x = in.readFloat();
        texturePoint.y = in.readFloat();
        textureSize.x = in.readFloat();
        textureSize.y = in.readFloat();
    }
    
    public void writeVisual(DataOutputStream out) throws IOException
    {
        out.writeFloat(color.r);
        out.writeFloat(color.g);
        out.writeFloat(color.b);
        out.writeFloat(color.a);
        
        if (animationName == null)
            out.writeByte(0);
        else
        {
            out.writeByte(1);
            out.writeUTF(animationName);
        }

        out.writeFloat(animationRoot.x);
        out.writeFloat(animationRoot.y);
        out.writeFloat(animationScale.x);
        out.writeFloat(animationScale.y);
        
        if (textureName == null)
            out.writeByte(0);
        else
        {
            out.writeByte(1);
            out.writeUTF(textureName);
        }

        out.writeFloat(texturePoint.x);
        out.writeFloat(texturePoint.y);
        out.writeFloat(textureSize.x);
        out.writeFloat(textureSize.y);
    }
    
    private boolean compareStringsWithNull(String s1, String s2)
    {
        if (s1 == null || s2 == null)
            return s1 == null && s2 == null;
        return s1.equals(s2);
    }
    
    private boolean compareFloatsWithNaN(float f1, float f2)
    {
        if (Float.isNaN(f1) || Float.isNaN(f2))
            return Float.isNaN(f1) && Float.isNaN(f2);
        return f1 == f2;
    }
    
    private boolean compareVectorsWithNaN(Vector2 v1, Vector2 v2)
    {
        return compareFloatsWithNaN(v1.x, v2.x) && compareFloatsWithNaN(v1.y, v2.y);
    }
}
