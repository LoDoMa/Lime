package net.lodoma.lime.world.physics;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.lodoma.lime.resource.animation.Animation;
import net.lodoma.lime.resource.texture.Texture;
import net.lodoma.lime.util.Vector2;
import net.lodoma.lime.world.gfx.Vertex;
import static org.lwjgl.opengl.GL11.*;

public class PhysicsShapeSnapshot
{
    public PhysicsShapeType shapeType;
    public Vector2 offset;
    public float radius;
    public Vector2[] vertices;
    
    public PhysicsShapeAttachments attachments;
    
    public void read(DataInputStream in) throws IOException
    {
        shapeType = PhysicsShapeType.values()[in.readInt()];
        switch (shapeType)
        {
        case CIRCLE:
            if (offset == null)
                offset = new Vector2();
            offset.x = in.readFloat();
            offset.y = in.readFloat();
            radius = in.readFloat();
            break;
        case POLYGON:
            int nVert = in.readInt();
            if (vertices == null || vertices.length != nVert)
                vertices = new Vector2[nVert];
            for (int i = 0; i < vertices.length; i++)
            {
                if (vertices[i] == null)
                    vertices[i] = new Vector2();
                vertices[i].x = in.readFloat();
                vertices[i].y = in.readFloat();
            }
            break;
        default:
            throw new IllegalStateException();
        }
        
        if (attachments == null)
            attachments = new PhysicsShapeAttachments();
        attachments.readVisual(in);
    }
    
    public void write(DataOutputStream out) throws IOException
    {
        out.writeInt(shapeType.ordinal());
        switch (shapeType)
        {
        case CIRCLE:
            out.writeFloat(offset.x);
            out.writeFloat(offset.y);
            out.writeFloat(radius);
            break;
        case POLYGON:
            out.writeInt(vertices.length);
            for (int i = 0; i < vertices.length; i++)
            {
                out.writeFloat(vertices[i].x);
                out.writeFloat(vertices[i].y);
            }
            break;
        default:
            throw new IllegalStateException();
        }
        
        attachments.writeVisual(out);
    }
    
    public boolean compare(PhysicsShapeSnapshot other)
    {
        if (shapeType != other.shapeType)
            return false;
        else if (!attachments.compareVisual(other.attachments))
            return false;
        else
            switch (shapeType)
            {
            case CIRCLE:
                if (!Vector2.equals(offset, other.offset) || radius != other.radius)
                    return false;
                break;
            case POLYGON: case TRIANGLE_GROUP:
                if (!Arrays.equals(vertices, other.vertices))
                    return false;
                break;
            }
        return true;
    }
    
    public void destroy()
    {
        if (attachments.textureName != null)
            Texture.referenceDown(attachments.textureName);
        if (attachments.animation != null)
            Animation.destroyAnimation(attachments.animation);
    }
    
    public void getVertices(List<Vertex> verts)
    {
        if (attachments.animationName != null)
        {
            List<Vertex> verts2 = new ArrayList<Vertex>();
            attachments.animation.getVertices(verts2);
            for (Vertex v : verts2)
            {
                v.x *= attachments.animationScale.x;
                v.y *= attachments.animationScale.y;
                if (shapeType == PhysicsShapeType.CIRCLE)
                {
                    v.x += offset.x;
                    v.y += offset.y;
                }
                v.x += attachments.animationRoot.x;
                v.y += attachments.animationRoot.y;
                v.r *= attachments.color.r;
                v.g *= attachments.color.g;
                v.b *= attachments.color.b;
                v.a *= attachments.color.a;
            }
            verts.addAll(verts2);
        }
        else
        {
            switch (shapeType)
            {
            case CIRCLE:
                float s0 = (-attachments.texturePoint.x) / attachments.textureSize.x;
                float t0 = (-attachments.texturePoint.y) / attachments.textureSize.y;
                for (int i = 0; i < 10; i++)
                {
                    float angle = (float) Math.toRadians(i * 360.0 / 10.0);
                    float angle2 = (float) Math.toRadians((i + 1) * 360.0 / 10.0);
                    float x = (float) Math.cos(angle);
                    float y = (float) Math.sin(angle);
                    float x2 = (float) Math.cos(angle2);
                    float y2 = (float) Math.sin(angle2);
                    float s1 = (x - attachments.texturePoint.x) / attachments.textureSize.x;
                    float t1 = (y - attachments.texturePoint.y) / attachments.textureSize.y;
                    float s2 = (x2 - attachments.texturePoint.x) / attachments.textureSize.x;
                    float t2 = (y2 - attachments.texturePoint.y) / attachments.textureSize.y;
                    verts.add(new Vertex().setXY(offset.x, offset.y).setRGBA(attachments.color).setST(s0, t0).setTexture(attachments.textureName));
                    verts.add(new Vertex().setXY(x * radius + offset.x, y * radius + offset.y).setRGBA(attachments.color).setST(s1, t1).setTexture(attachments.textureName));
                    verts.add(new Vertex().setXY(x2 * radius + offset.x, y2 * radius + offset.y).setRGBA(attachments.color).setST(s2, t2).setTexture(attachments.textureName));
                }
                break;
            case POLYGON:
            {
                for (int i = 0; i < vertices.length; i++)
                {
                    float s = (vertices[i].x - attachments.texturePoint.x) / attachments.textureSize.x;
                    float t = (vertices[i].y - attachments.texturePoint.y) / attachments.textureSize.y;
                    verts.add(new Vertex().setXY(vertices[i].x, vertices[i].y).setRGBA(attachments.color).setST(s, t).setTexture(attachments.textureName));
                }
                break;
            }
            default:
                throw new IllegalStateException();
            }
        }
    }
    
    public void debugRender()
    {
        switch (shapeType)
        {
        case CIRCLE:
        {
            glTranslatef(offset.x, offset.y, 0.0f);
            glScalef(radius, radius, 1.0f);
            Texture.NO_TEXTURE.bind(0);
            glColor3f(0.2f, 1.0f, 0.2f);
            glBegin(GL_LINE_LOOP);
            for (int i = 0; i <= 10; i++)
            {
                float angle = (float) Math.toRadians(i * 360.0 / 10.0);
                float x = (float) Math.cos(angle);
                float y = (float) Math.sin(angle);
                glVertex2f(x, y);
            }
            glEnd();
            glScalef(1.0f / radius, 1.0f / radius, 1.0f);
            glTranslatef(-offset.x, -offset.y, 0.0f);
            break;
        }
        case POLYGON:
        {
            Texture.NO_TEXTURE.bind(0);
            glColor3f(0.2f, 1.0f, 0.2f);
            glBegin(GL_LINE_LOOP);
            for (int i = 0; i < vertices.length; i++)
                glVertex2f(vertices[i].x, vertices[i].y);
            glEnd();
            break;
        }
        default:
            throw new IllegalStateException();
        }
    }
}
