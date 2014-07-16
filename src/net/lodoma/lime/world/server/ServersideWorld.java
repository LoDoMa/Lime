package net.lodoma.lime.world.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.lodoma.lime.physics.PhysicsWorld;
import net.lodoma.lime.physics.ServersidePhysicsPool;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.world.TileGrid;
import net.lodoma.lime.world.material.Material;

public class ServersideWorld implements TileGrid
{
    /* limit: NetworkSettings.MAX_PACKET_SIZE */
    private static final int CHUNKW = 30;
    private static final int CHUNKH = 30;
    
    @SuppressWarnings("unused")
    private Server server;
    
    private int width;
    private int height;
    
    private int chunkAX;
    private int chunkAY;
    
    private Map<Short, Material> palette;
    private WorldChunk[] chunks;
    
    private boolean paletteLock;
    
    private PhysicsWorld physicsWorld;
    
    public ServersideWorld(Server server)
    {
        this.server = server;
        this.palette = new HashMap<Short, Material>();
        
        physicsWorld = new PhysicsWorld(new ServersidePhysicsPool());
    }
    
    public void fetch()
    {
        physicsWorld.getPool().fetch();
    }
    
    public void init(int width, int height)
    {
        chunkAX = width / CHUNKW + ((width % CHUNKW != 0) ? 1 : 0);
        chunkAY = height / CHUNKH + ((height % CHUNKH != 0) ? 1 : 0);
        chunks = new WorldChunk[chunkAX * chunkAY];
        
        this.width = chunkAX * CHUNKW;
        this.height = chunkAY * CHUNKH;
        
        for(int y = 0; y < chunkAY; y++)
            for(int x = 0; x < chunkAX; x++)
                chunks[y * chunkAX + x] = new WorldChunk(CHUNKW, CHUNKH);
        
        paletteLock = false;
    }
    
    public PhysicsWorld getPhysicsWorld()
    {
        return physicsWorld;
    }
    
    public int getWidth()
    {
        return width;
    }
    
    public int getHeight()
    {
        return height;
    }
    
    public int getChunkHC()
    {
        return chunkAX;
    }
    
    public int getChunkVC()
    {
        return chunkAY;
    }
    
    public Set<Short> getPaletteKeySet()
    {
        return new HashSet<Short>(palette.keySet());
    }
    
    public Material getPaletteMember(short paletteKey)
    {
        return palette.get(paletteKey);
    }
    
    public void setPaletteMember(short paletteKey, Material material)
    {
        if(paletteLock)
            throw new LockedPaletteModificationException();
        palette.put(paletteKey, material);
    }
    
    public void removePaletteMember(short paletteKey)
    {
        if(paletteLock)
            throw new LockedPaletteModificationException();
        palette.remove(paletteKey);
    }
    
    public void clearPalette()
    {
        if(paletteLock)
            throw new LockedPaletteModificationException();
        palette.clear();
    }
    
    public void lockPaletteState()
    {
        paletteLock = true;
    }
    
    @Override
    public byte getTileInfo(int x, int y)
    {
        return chunks[(y / CHUNKH) * chunkAX + (x / CHUNKW)].getInfo(x % CHUNKW, y % CHUNKH);
    }
    
    @Override
    public byte getTileShape(int x, int y)
    {
        return chunks[(y / CHUNKH) * chunkAX + (x / CHUNKW)].getShape(x % CHUNKW, y % CHUNKH);
    }
    
    @Override
    public short getTileMaterial(int x, int y)
    {
        return chunks[(y / CHUNKH) * chunkAX + (x / CHUNKW)].getMaterial(x % CHUNKW, y % CHUNKH);
    }
    
    @Override
    public void setTileInfo(int x, int y, byte info)
    {
        chunks[(y / CHUNKH) * chunkAX + (x / CHUNKW)].setInfo(x % CHUNKW, y % CHUNKH, info);
    }
    
    @Override
    public void setTileShape(int x, int y, byte shape)
    {
        chunks[(y / CHUNKH) * chunkAX + (x / CHUNKW)].setShape(x % CHUNKW, y % CHUNKH, shape);
    }
    
    @Override
    public void setTileMaterial(int x, int y, short material)
    {
        chunks[(y / CHUNKH) * chunkAX + (x / CHUNKW)].setMaterial(x % CHUNKW, y % CHUNKH, material);
    }
    
    public void lockChunkState()
    {
        for(int y = 0; y < chunkAY; y++)
            for(int x = 0; x < chunkAX; x++)
                chunks[y * chunkAX + x].lockState();
    }
}
