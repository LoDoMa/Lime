package net.lodoma.lime.world;

import net.lodoma.lime.server.generic.GenericServer;
import net.lodoma.lime.server.generic.UserPool;

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
}
