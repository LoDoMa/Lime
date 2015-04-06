package net.lodoma.lime.world.physics;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import net.lodoma.lime.resource.animation.Animation;
import net.lodoma.lime.resource.texture.Texture;
import net.lodoma.lime.util.Vector2;
import static org.lwjgl.opengl.GL11.*;

public class PhysicsShapeSnapshot
{
    public PhysicsShapeType shapeType;
    public Vector2 offset;
    public float radius;
    public Vector2[] vertices;
    
    public PhysicsShapeAttachments attachments;

    private int displayList;
    private boolean firstDisplayList = true;
    private boolean recreateDisplayList;
    
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
        
        recreateDisplayList = true;
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
    
    private void createDisplayList()
    {
        displayList = glGenLists(1);
        glNewList(displayList, GL_COMPILE);

        attachments.color.setGL();
        
        switch (shapeType)
        {
        case CIRCLE:
        {
            glTranslatef(offset.x, offset.y, 0.0f);
            glScalef(radius, radius, 1.0f);
            
            glBegin(GL_TRIANGLE_FAN);
            glVertex2f(0.0f, 0.0f);
            for (int i = 0; i <= 10; i++)
            {
                float angle = (float) Math.toRadians(i * 360.0 / 10.0);
                float x = (float) Math.cos(angle);
                float y = (float) Math.sin(angle);
                float texx = (x - attachments.texturePoint.x) / attachments.textureSize.x;
                float texy = (y - attachments.texturePoint.y) / attachments.textureSize.y;
                glTexCoord2f(texx, -texy);
                glVertex2f(x, y);
            }
            glEnd();
            break;
        }
        case POLYGON:
        {
            glBegin(GL_POLYGON);
            for (int i = 0; i < vertices.length; i++)
            {
                float texx = (vertices[i].x - attachments.texturePoint.x) / attachments.textureSize.x;
                float texy = (vertices[i].y - attachments.texturePoint.y) / attachments.textureSize.y;
                glTexCoord2f(texx, -texy);
                glVertex2f(vertices[i].x, vertices[i].y);
            }
            glEnd();
            break;
        }
        default:
            throw new IllegalStateException();
        }
        
        glEndList();
    }
    
    public void render()
    {
        if (attachments.animationName != null)
        {
            if (shapeType == PhysicsShapeType.CIRCLE)
                glTranslatef(offset.x, offset.y, 0.0f);
            
            glTranslatef(attachments.animationRoot.x, attachments.animationRoot.y, 0.0f);
            glScalef(attachments.animationScale.x, attachments.animationScale.y, 1.0f);

            attachments.color.setGL();
            attachments.animation.render();
            
            glScalef(1.0f / attachments.animationScale.x, 1.0f / attachments.animationScale.y, 1.0f);
            glTranslatef(-attachments.animationRoot.x, -attachments.animationRoot.y, 0.0f);
        }
        else
        {
            if (recreateDisplayList)
            {
                if (firstDisplayList)
                {
                    glDeleteLists(displayList, 1);
                    firstDisplayList = false;
                }
                createDisplayList();
                recreateDisplayList = false;
            }

            if (attachments.textureName == null)
                Texture.NO_TEXTURE.bind(0);
            else
                Texture.get(attachments.textureName).bind(0);
            
            glCallList(displayList);
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
