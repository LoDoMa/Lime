package net.lodoma.lime.world;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.lodoma.lime.util.BinaryHelper;

public class World
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
    
    private int width;
    private int height;
    
    private Map<Short, Material> materialMap;
    
    private byte[] tileInfo;
    private byte[] tileShape;
    private short[] tileMaterial;
    
    private Set<Long> refreshTileSet;
    
    public World()
    {
        materialMap = new HashMap<Short, Material>();
        refreshTileSet = new HashSet<Long>();
    }
    
    public void init(int width, int height)
    {
        this.width = width;
        this.height = height;
        
        materialMap.clear();
        
        tileInfo = new byte[width * height];
        tileShape = new byte[width * height];
        tileMaterial = new short[width * height];
        
        refreshTileSet.clear();
    }
    
    public Material getMaterial(short id)
    {
        return materialMap.get(id);
    }
    
    public byte getTileInfo(int x, int y)
    {
        return tileInfo[y * width + x];
    }
    
    public byte getTileShape(int x, int y)
    {
        return tileShape[y * width + x];
    }
    
    public short getTileMaterial(int x, int y)
    {
        return tileMaterial[y * width + x];
    }
    
    public void setMaterialID(short id, Material material)
    {
        materialMap.put(id, material);
    }
    
    public void setTileInfo(int x, int y, byte info)
    {
        tileInfo[y * width + x] = info;
    }
    
    public void setTileShape(int x, int y, byte shape)
    {
        tileShape[y * width + x] = shape;
    }
    
    public void setTileMaterial(int x, int y, short material)
    {
        tileMaterial[y * width + x] = material;
    }
    
    public void update(float timeDelta)
    {
        
    }
    
    public void render()
    {
        
    }
}
