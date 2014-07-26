package net.lodoma.lime.world.platform;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.lodoma.lime.mask.ColoredMask;
import net.lodoma.lime.mask.Mask;
import net.lodoma.lime.physics.PhysicsBody;
import net.lodoma.lime.physics.PhysicsBodyType;
import net.lodoma.lime.physics.PhysicsWorld;
import net.lodoma.lime.util.Vector2;

public class Platform
{
    private Mask mask;
    private PhysicsBody body;
    
    private Vector2 offset;
    private Vector2[] vertices;
    
    public Platform(DataInputStream inputStream) throws IOException
    {
        int n = inputStream.readInt();
        offset = new Vector2(inputStream.readFloat(), inputStream.readFloat());
        vertices = new Vector2[n];
        
        float x[] = new float[n];
        float y[] = new float[n];
        float r[] = new float[n];
        float g[] = new float[n];
        float b[] = new float[n];
        float a[] = new float[n];
        for(int i = 0; i < n; i++)
        {
            vertices[i] = new Vector2(inputStream.readFloat(), inputStream.readFloat());
            x[i] = vertices[i].x;
            y[i] = vertices[i].y;
            r[i] = 1.0f;
            g[i] = 1.0f;
            b[i] = 1.0f;
            a[i] = 1.0f;
        }
        this.mask = new ColoredMask(n, x, y, r, g, b, a);
        
        body = new PhysicsBody();
        body.setBodyType(PhysicsBodyType.STATIC);
        body.setPolygonShape(vertices);
        body.setDefPosition(offset);
    }
    
    public Platform(Mask mask, Vector2 offset, Vector2... vertices)
    {
        this.mask = mask;
        this.offset = offset;
        this.vertices = vertices;
        
        body = new PhysicsBody();
        body.setBodyType(PhysicsBodyType.STATIC);
        body.setPolygonShape(vertices);
        body.setDefPosition(offset);
    }
    
    public Platform(Vector2 offset, Vector2... vertices)
    {
        this(null, offset, vertices);
    }
    
    public void setMask(Mask mask)
    {
        this.mask = mask;
    }
    
    public void create(PhysicsWorld world)
    {
        body.create(world);
    }
    
    public void destroy(PhysicsWorld world)
    {
        body.destroy(world);
    }
    
    public void render()
    {
        if(mask != null)
            mask.render();
    }
    
    public void writeToStream(DataOutputStream outputStream) throws IOException
    {
        outputStream.writeInt(vertices.length);
        outputStream.writeFloat(offset.x);
        outputStream.writeFloat(offset.y);
        for(int i = 0; i < vertices.length; i++)
        {
            outputStream.writeFloat(vertices[i].x);
            outputStream.writeFloat(vertices[i].y);
        }
    }
}
