package net.lodoma.lime.world;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.lodoma.lime.client.generic.net.GenericClient;
import net.lodoma.lime.client.generic.net.packet.ClientPacketPool;
import net.lodoma.lime.common.net.NetStage;
import net.lodoma.lime.texture.TexturePool;
import net.lodoma.lime.util.BinaryHelper;
import net.lodoma.lime.world.material.Material;

import org.lwjgl.opengl.GL11;

/* Disable formatting
 * @formatter:off
 */
public class ClientsideWorld implements TileGrid
{
    public static final int MASK_TILEINFO_METADATA = 0b00000001;
    public static final int MASK_TILEINFO_REFRESHED = 0b00000010;
    public static final int MASK_TILEINFO_SOLID = 0b00000100;
    
    public static final int MASK_TILESHAPE_BOTTOM_LEFT = 0b00000001;
    public static final int MASK_TILESHAPE_BOTTOM_MIDDLE = 0b00000010;
    public static final int MASK_TILESHAPE_BOTTOM_RIGHT = 0b00000100;
    public static final int MASK_TILESHAPE_MIDDLE_RIGHT = 0b00001000;
    public static final int MASK_TILESHAPE_TOP_RIGHT = 0b00010000;
    public static final int MASK_TILESHAPE_TOP_MIDDLE = 0b00100000;
    public static final int MASK_TILESHAPE_TOP_LEFT = 0b01000000;
    public static final int MASK_TILESHAPE_MIDDLE_LEFT = 0b10000000;
    
    public static byte buildTileShape(boolean... presence)
    {
        byte tileshape = 0;
        int count = presence.length < 8 ? presence.length : 8;
        for(int i = 0; i < count; i++)
            if(presence[i])
                tileshape = BinaryHelper.setOn(tileshape, 1 << i);
        return tileshape;
    }
    
    public static final byte TILESHAPE_FULL = buildTileShape(true, false, true, false, true, false, true, false);
    public static final byte TILESHAPE_SLOPELEFT = buildTileShape(true, false, true, false, true, false, false, false);
    public static final byte TILESHAPE_SLOPERIGHT = buildTileShape(true, false, true, false, false, false, true, false);
    public static final byte TILESHAPE_SLOPELEFT_YFLIP = buildTileShape(false, false, true, false, true, false, true, false);
    public static final byte TILESHAPE_SLOPERIGHT_YFLIP = buildTileShape(true, false, false, false, true, false, true, false);
    public static final byte TILESHAPE_SLAB = buildTileShape(true, false, true, true, false, false, false, true);
    public static final byte TILESHAPE_SLAB_YFLIP = buildTileShape(false, false, false, true, true, false, true, true);
    public static final byte TILESHAPE_PILLAR = buildTileShape(true, true, false, false, false, true, true, false);
    public static final byte TILESHAPE_PILLAR_XFLIP = buildTileShape(false, true, true, false, true, true, false, false);
    public static final byte TILESHAPE_EMPTY = buildTileShape(false, false, false, false, false, false, false, false);
    
    private GenericClient client;
    private ClientPacketPool packetPool;
    private TexturePool texturePool;
    private boolean startedSequence;
    
    private int width;
    private int height;
    
    private byte[] tileInfo;
    private byte[] tileShape;
    private short[] tileMaterial;
    
    private Map<Short, Material> palette;
    
    public ClientsideWorld(GenericClient client)
    {
        this.client = client;
        palette = new HashMap<Short, Material>();
        reset();
    }
    
    public void fetch()
    {
        packetPool = (ClientPacketPool) client.getProperty("packetPool");
        texturePool = (TexturePool) client.getProperty("texturePool");
    }
    
    public void reset()
    {
        startedSequence = false;
        
        width = 0;
        height = 0;
        
        tileInfo = null;
        tileShape = null;
        tileMaterial = null;
        
        palette.clear();
    }
    
    public void receiveDimensions(int width, int height)
    {
        this.width = width;
        this.height = height;
        
        this.tileInfo = new byte[width * height];
        this.tileShape = new byte[width * height];
        this.tileMaterial = new short[width * height];
        
        packetPool.getPacket("Lime::WorldPaletteRequest").send(client);
    }
    
    public void receivePalette(byte[] content)
    {
        palette.clear();
        ByteBuffer buffer = ByteBuffer.wrap(content);
        int count = buffer.getInt();
        while((count--) != 0)
        {
            short key = buffer.getShort();
            long least = buffer.getLong();
            long most = buffer.getLong();
            UUID uuid = new UUID(most, least);
            System.out.println("material " + key + ": " + uuid);
            Material material = new Material(uuid);
            palette.put(key, material);
        }
        
        packetPool.getPacket("Lime::WorldChunksRequest").send(client);
    }
    
    public void receiveChunk(int cx, int cy, int cw, int ch, byte[] content)
    {
        ByteBuffer buffer = ByteBuffer.wrap(content);
        for(int y = 0; y < ch; y++)
            for(int x = 0; x < cw; x++)
            {
                setTileInfo(x + cx, y + cy, buffer.get());
                setTileShape(x + cx, y + cy, buffer.get());
                setTileMaterial(x + cx, y + cy, buffer.getShort());
            }
    }
    
    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }
    
    @Override
    public byte getTileInfo(int x, int y)
    {
        return tileInfo[y * width + x];
    }

    @Override
    public byte getTileShape(int x, int y)
    {
        return tileShape[y * width + x];
    }

    @Override
    public short getTileMaterial(int x, int y)
    {
        return tileMaterial[y * width + x];
    }
    
    @Override
    public void setTileInfo(int x, int y, byte info)
    {
        tileInfo[y * width + x] = info;
    }
    
    @Override
    public void setTileShape(int x, int y, byte shape)
    {
        tileShape[y * width + x] = shape;
    }
    
    @Override
    public void setTileMaterial(int x, int y, short material)
    {
        tileMaterial[y * width + x] = material;
    }
    
    public void update(float timeDelta)
    {
        if(!startedSequence && ((NetStage) client.getProperty("networkStage")) == NetStage.USER)
        {
            packetPool.getPacket("Lime::WorldDimensionRequest").send(client);
            startedSequence = true;
        }
    }
    
    public void render()
    {
        if(tileInfo != null && tileShape != null && tileMaterial != null)
        {
            // tiles *should* be safe to render at this point
            
            // TODO: use display lists
            for(int y = 0; y < height; y++)
                for(int x = 0; x < width; x++)
                {
                    short tileMaterial = getTileMaterial(x, y);
                    Material material = palette.get(tileMaterial);
                    
                    if(material == null) continue;
                    
                    if(!material.rendered) continue;
                    if(material.texture != 0)
                        texturePool.getTexture(material.texture).bind(0);
                    
                    byte tileShape = getTileShape(x, y);
                    GL11.glBegin(GL11.GL_POLYGON);
                    if((tileShape & MASK_TILESHAPE_BOTTOM_LEFT) != 0)
                        {GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex2f(x + 0.0f, y + 0.0f);}
                    if((tileShape & MASK_TILESHAPE_BOTTOM_MIDDLE) != 0)
                        {GL11.glTexCoord2f(0.5f, 1.0f); GL11.glVertex2f(x + 0.5f, y + 0.0f);}
                    if((tileShape & MASK_TILESHAPE_BOTTOM_RIGHT) != 0)
                        {GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex2f(x + 1.0f, y + 0.0f);}
                    if((tileShape & MASK_TILESHAPE_MIDDLE_RIGHT) != 0)
                        {GL11.glTexCoord2f(1.0f, 0.5f); GL11.glVertex2f(x + 1.0f, y + 0.5f);}
                    if((tileShape & MASK_TILESHAPE_TOP_RIGHT) != 0)
                        {GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex2f(x + 1.0f, y + 1.0f);}
                    if((tileShape & MASK_TILESHAPE_TOP_MIDDLE) != 0)
                        {GL11.glTexCoord2f(0.5f, 0.0f); GL11.glVertex2f(x + 0.5f, y + 1.0f);}
                    if((tileShape & MASK_TILESHAPE_TOP_LEFT) != 0)
                        {GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex2f(x + 0.0f, y + 1.0f);}
                    if((tileShape & MASK_TILESHAPE_MIDDLE_LEFT) != 0)
                        {GL11.glTexCoord2f(0.0f, 0.5f); GL11.glVertex2f(x + 0.0f, y + 0.5f);}
                    GL11.glEnd();
                }
        }
    }
}
