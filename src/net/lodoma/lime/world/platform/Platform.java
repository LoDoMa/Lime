package net.lodoma.lime.world.platform;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import net.lodoma.lime.mask.Mask;
import net.lodoma.lime.physics.PhysicsWorld;
import net.lodoma.lime.util.Vector2;

public class Platform
{
    private Mask mask;
    
    private Vector2[] vertices;
    
    public static Platform newInstance(PhysicsWorld world, Vector2 offset, List<Vector2> vertexList)
    {
        Vector2[] vertices = new Vector2[vertexList.size()];
        vertexList.toArray(vertices);
        return new Platform(world, offset, vertices);
    }
    
    public Platform(PhysicsWorld world, Vector2 offset, Vector2[] vertices)
    {
        Vector2[] offsetVertices = new Vector2[vertices.length];
        for(int i = 0; i < offsetVertices.length; i++)
            offsetVertices[i] = vertices[i].add(offset);
        this.vertices = offsetVertices;
    }
    
    public Platform(PhysicsWorld world, DataInputStream inputStream) throws IOException
    {
        int n = inputStream.readInt();
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
        
        //this.mask = new ColoredMask(n, x, y, r, g, b, a);
    }
    
    public void destroy(PhysicsWorld world)
    {
        
    }
    
    public void setMask(Mask mask)
    {
        this.mask = mask;
    }
    
    public void render()
    {
        if(mask != null)
            mask.render();
    }
    
    public void writeToStream(DataOutputStream outputStream) throws IOException
    {
        outputStream.writeInt(vertices.length);
        for(int i = 0; i < vertices.length; i++)
        {
            outputStream.writeFloat(vertices[i].x);
            outputStream.writeFloat(vertices[i].y);
        }
    }
}
