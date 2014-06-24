package net.lodoma.lime.world.server;

import java.nio.ByteBuffer;

public class WorldChunk
{
    private int width;
    private int height;
    
    private byte[] info;
    private byte[] shape;
    private short[] material;
    
    private boolean locked;
    
    public WorldChunk(int width, int height)
    {
        this.width = width;
        this.height = height;
        
        this.info = new byte[width * height];
        this.shape = new byte[width * height];
        this.material = new short[width * height];
        
        this.locked = false;
    }
    
    public void setInfo(int x, int y, byte v)
    {
        if(locked)
            throw new LockedChunkModificationException();
        info[y * width + x] = v;
    }
    
    public void setShape(int x, int y, byte v)
    {
        if(locked)
            throw new LockedChunkModificationException();
        shape[y * width + x] = v;
    }
    
    public void setMaterial(int x, int y, short v)
    {
        if(locked)
            throw new LockedChunkModificationException();
        material[y * width + x] = v;
    }
    
    public void lockState()
    {
        locked = true;
    }
    
    public byte getInfo(int x, int y)
    {
        return info[y * width + x];
    }
    
    public byte getShape(int x, int y)
    {
        return shape[y * width + x];
    }
    
    public short getMaterial(int x, int y)
    {
        return material[y * width + x];
    }
    
    public ByteBuffer build()
    {
        ByteBuffer buffer = ByteBuffer.allocate(width * height * 4);
        for(int y = 0; y < height; y++)
            for(int x = 0; x < width; x++)
            {
                buffer.put(info[y * width + x]);
                buffer.put(shape[y * width + x]);
                buffer.putShort(material[y * width + x]);
            }
        return buffer;
    }
}
