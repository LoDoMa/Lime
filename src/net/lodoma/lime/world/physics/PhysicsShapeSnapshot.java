package net.lodoma.lime.world.physics;

import net.lodoma.lime.texture.Texture;
import net.lodoma.lime.texture.TexturePool;
import net.lodoma.lime.util.Vector2;
import static org.lwjgl.opengl.GL11.*;

public class PhysicsShapeSnapshot
{
    public PhysicsShapeType shapeType;
    public Vector2 offset;
    public float radius;
    public Vector2[] vertices;
    
    public PhysicsShapeAttachments attachments;
    
    public void debugRender()
    {
        Texture texture = Texture.NO_TEXTURE;
        if (attachments.animation == null && attachments.textureName != null)
        {
            texture = TexturePool.get(attachments.textureName);
            if (texture == null)
            {
                TexturePool.add(attachments.textureName);
                texture = TexturePool.get(attachments.textureName);
            }
        }
        
        switch (shapeType)
        {
        case CIRCLE:
        {
            glTranslatef(offset.x, offset.y, 0.0f);
            attachments.color.setGL();
            texture.bind();
            
            if (attachments.animation != null)
            {
                glTranslatef(attachments.animationRoot.x, attachments.animationRoot.y, 0.0f);
                glScalef(attachments.animationScale.x, attachments.animationScale.y, 1.0f);

                attachments.animation.render();
                
                glScalef(1.0f / attachments.animationScale.x, 1.0f / attachments.animationScale.y, 1.0f);
                glTranslatef(-attachments.animationRoot.x, -attachments.animationRoot.y, 0.0f);
            }
            else
            {
                glScalef(radius, radius, 1.0f);
                
                glBegin(GL_TRIANGLE_FAN);
    
                glVertex2f(0.0f, 0.0f);
                for (int i = 0; i <= 10; i++)
                {
                    float angle = (float) Math.toRadians(i * 360.0 / 10.0);
                    float x = (float) Math.cos(angle);
                    float y = (float) Math.sin(angle);
                    if (attachments.textureName != null)
                    {
                        float texx = (x - attachments.texturePoint.x) / attachments.textureSize.x;
                        float texy = (y - attachments.texturePoint.y) / attachments.textureSize.y;
                        glTexCoord2f(texx, -texy);
                    }
                    else
                        glTexCoord2f(0.0f, 0.0f);
                    glVertex2f(x, y);
                }
              
                glEnd();
                
                glScalef(1.0f / radius, 1.0f / radius, 1.0f);
            }
            
            glTranslatef(-offset.x, -offset.y, 0.0f);
            break;
        }
        case POLYGON:
        {
            attachments.color.setGL();
            texture.bind();

            if (attachments.animation != null)
            {
                glTranslatef(attachments.animationRoot.x, attachments.animationRoot.y, 0.0f);
                glScalef(attachments.animationScale.x, attachments.animationScale.y, 1.0f);
                
                attachments.animation.render();
                
                glScalef(1.0f / attachments.animationScale.x, 1.0f / attachments.animationScale.y, 1.0f);
                glTranslatef(-attachments.animationRoot.x, -attachments.animationRoot.y, 0.0f);
            }
            else
            {
                glBegin(GL_POLYGON);
    
                for (int i = 0; i < vertices.length; i++)
                {
                    if (attachments.textureName != null)
                    {
                        float texx = (vertices[i].x - attachments.texturePoint.x) / attachments.textureSize.x;
                        float texy = (vertices[i].y - attachments.texturePoint.y) / attachments.textureSize.y;
                        glTexCoord2f(texx, -texy);
                    }
                    else
                        glTexCoord2f(0.0f, 0.0f);
                    glVertex2f(vertices[i].x, vertices[i].y);
                }
              
                glEnd();
            }
            
            break;
        }
        default:
            throw new IllegalStateException();
        }
    }
}
