package net.lodoma.lime.world;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

public class World
{
    private int width;
    private int height;
    
    private Map<Short, Material> materialMap;
    
    private byte[] tileInfo;
    private byte[] tileShape;
    private short[] tileMaterial;
    
    public World()
    {
        materialMap = new HashMap<Short, Material>();
    }
    
    public void init(int width, int height)
    {
        this.width = width;
        this.height = height;

        materialMap.clear();
        
        tileInfo = new byte[width * height];
        tileShape = new byte[width * height];
        tileMaterial = new short[width * height];
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
