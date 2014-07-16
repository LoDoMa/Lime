package net.lodoma.lime.world.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientOutput;
import net.lodoma.lime.common.net.NetStage;
import net.lodoma.lime.util.BinaryHelper;
import net.lodoma.lime.util.HashPool;
import net.lodoma.lime.world.TileGrid;
import net.lodoma.lime.world.entity.Entity;
import net.lodoma.lime.world.material.Material;

import org.jbox2d.dynamics.World;

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
    
    private Client client;
    private HashPool<ClientOutput> coPool;
    private boolean startedSequence;
    
    private int width;
    private int height;
    
    private byte[] tileInfo;
    private byte[] tileShape;
    private short[] tileMaterial;
    
    private Map<Short, Material> palette;

    private boolean renderReady;
    private boolean firstRender;
    private WorldRenderer renderer;
    
    private World world;
    private Set<Entity> entities;
    
    public ClientsideWorld(Client client)
    {
        this.client = client;
        palette = new HashMap<Short, Material>();
        renderer = new WorldRenderer(this);
        entities = new HashSet<Entity>();

        startedSequence = false;
        reset(0, 0);
    }

    @SuppressWarnings("unchecked")
    public void fetch()
    {
        coPool = (HashPool<ClientOutput>) this.client.getProperty("coPool");
    }
    
    public void reset(int width, int height)
    {
        this.width = width;
        this.height = height;
        
        tileInfo = new byte[width * height];
        tileShape = new byte[width * height];
        tileMaterial = new short[width * height];
        
        palette.clear();

        renderReady = false;
        firstRender = true;
        
        for(Entity entity : entities)
            entity.destroy();
        entities.clear();
    }
    
    public void requestInitialData()
    {
        startedSequence = false;
    }
    
    public void setRenderReady()
    {
        renderReady = true;
    }
    
    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }
    
    public void addPaletteMember(short key, Material material)
    {
        palette.put(key, material);
    }
    
    public int getPaletteSize()
    {
        return palette.size();
    }
    
    public Material getPaletteMaterial(short key)
    {
        return palette.get(key);
    }
    
    public List<Material> getPaletteValues()
    {
        return new ArrayList<Material>(palette.values());
    }
    
    public Set<Short> getPaletteKeys()
    {
        return new HashSet<Short>(palette.keySet());
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
    
    public World getWorld()
    {
        return world;
    }
    
    public Set<Entity> getEntitySet()
    {
        return new HashSet<Entity>(entities);
    }
    
    public void addEntity(Entity entity)
    {
        entities.add(entity);
    }
    
    public void removeEntity(Entity entity)
    {
        entities.remove(entity);
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
            coPool.get("Lime::InitialWorldRequest").handle();
            startedSequence = true;
        }
    }
    
    public void render()
    {
        if(renderReady)
        {
            if(firstRender)
            {
                renderer.recompile();
                firstRender = false;
            }
            
            renderer.render();
        }
    }
}
