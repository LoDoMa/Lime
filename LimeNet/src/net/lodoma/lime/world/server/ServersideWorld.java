package net.lodoma.lime.world.server;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.lodoma.lime.server.generic.GenericServer;
import net.lodoma.lime.server.generic.ServerUser;
import net.lodoma.lime.server.generic.net.packet.ServerPacketPool;
import net.lodoma.lime.world.TileGrid;
import net.lodoma.lime.world.material.Material;

public class ServersideWorld implements TileGrid
{
    /* limit: NetworkSettings.MAX_PACKET_SIZE */
    private static final int CHUNKW = 30;
    private static final int CHUNKH = 30;
    
    private GenericServer server;
    private ServerPacketPool packetPool;
    
    private int width;
    private int height;
    
    private int chunkAX;
    private int chunkAY;
    
    private Map<Short, Material> palette;
    private WorldChunk[] chunks;
    
    private boolean paletteLock;
    
    public ServersideWorld(GenericServer server)
    {
        this.server = server;
        this.palette = new HashMap<Short, Material>();
    }
    
    public void fetch()
    {
        packetPool = (ServerPacketPool) server.getProperty("packetPool");
    }
    
    public void init(int width, int height)
    {
        this.width = width;
        this.height = height;
        
        chunkAX = width / CHUNKW + ((width % CHUNKW != 0) ? 1 : 0);
        chunkAY = height / CHUNKH + ((height % CHUNKH != 0) ? 1 : 0);
        chunks = new WorldChunk[chunkAX * chunkAY];
        
        for(int y = 0; y < chunkAY; y++)
        {
            int ch = (height - y * CHUNKH) < CHUNKH ? height % CHUNKH : CHUNKH;
            for(int x = 0; x < chunkAX; x++)
            {
                int cw = (width - x * CHUNKW) < CHUNKW ? width % CHUNKW : CHUNKW;
                chunks[y * chunkAX + x] = new WorldChunk(cw, ch);
            }
        }
        
        paletteLock = false;
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
    
    private void sendChunkPacket(int x, int y, ServerUser user)
    {
        ByteBuffer chunkBuffer = chunks[y * chunkAX + x].build();
        byte[] chunkBytes = chunkBuffer.array();
        
        packetPool.getPacket("Lime::WorldChunk").send(server, user, x * CHUNKW, y * CHUNKH, CHUNKW, CHUNKH, chunkBytes);
    }
    
    public void sendChunkPackets(ServerUser user)
    {
        for(int y = 0; y < chunkAY; y++)
            for(int x = 0; x < chunkAX; x++)
                sendChunkPacket(x, y, user);
    }
}
