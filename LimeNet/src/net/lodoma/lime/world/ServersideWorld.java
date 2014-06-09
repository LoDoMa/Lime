package net.lodoma.lime.world;

import net.lodoma.lime.server.generic.GenericServer;
import net.lodoma.lime.server.generic.UserPool;
import net.lodoma.lime.world.material.Material;

public class ServersideWorld
{
    private GenericServer server;
    private UserPool userPool;
    
    private int width;
    private int height;

    private int chunkw;
    private int chunkh;
    private int chunkAX;
    private int chunkAY;
    
    private WorldChunk[] chunks;
    
    public ServersideWorld(GenericServer server)
    {
        this.server = server;
    }
    
    public void fetch()
    {
        userPool = (UserPool) server.getProperty("userPool");
    }
    
    public void init(int width, int height, int chunkw, int chunkh)
    {
        this.width = width;
        this.height = height;

        this.chunkw = chunkw;
        this.chunkh = chunkh;
        chunkAX = width / chunkw + ((width % chunkw != 0) ? 1 : 0);
        chunkAY = height / chunkh + ((height % chunkh != 0) ? 1 : 0);
        chunks = new WorldChunk[chunkAX * chunkAY];
        
        for(int y = 0; y < chunkAY; y++)
        {
            int ch = (height - y * chunkh) % chunkh;
            for(int x = 0; x < chunkAX; x++)
            {
                int cw = (width - x * chunkw) % chunkw;
                chunks[y * chunkAX + x] = new WorldChunk(this, cw, ch, userPool);
            }
        }
    }
    
    public byte getTileInfo(int x, int y)
    {
        return chunks[(y / chunkh) * chunkAX + (x / chunkw)].getInfo(x % chunkw, y % chunkh);
    }
    
    public byte getTileShape(int x, int y)
    {
        return chunks[(y / chunkh) * chunkAX + (x / chunkw)].getShape(x % chunkw, y % chunkh);
    }
    
    public short getTileMaterial(int x, int y)
    {
        return chunks[(y / chunkh) * chunkAX + (x / chunkw)].getMaterial(x % chunkw, y % chunkh);
    }
    
    public void setTileInfo(int x, int y, byte info)
    {
        chunks[(y / chunkh) * chunkAX + (x / chunkw)].setInfo(x % chunkw, y % chunkh, info);
    }
    
    public void setTileShape(int x, int y, byte shape)
    {
        chunks[(y / chunkh) * chunkAX + (x / chunkw)].setShape(x % chunkw, y % chunkh, shape);
    }
    
    public void setTileMaterial(int x, int y, short material)
    {
        chunks[(y / chunkh) * chunkAX + (x / chunkw)].setMaterial(x % chunkw, y % chunkh, material);
    }
    
    public void update(float timeDelta)
    {
        for(int y = 0; y < chunkAY; y++)
            for(int x = 0; x < chunkAX; x++)
                chunks[y * chunkAX + x].update();
    }
}
