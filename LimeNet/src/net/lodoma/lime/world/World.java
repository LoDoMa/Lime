package net.lodoma.lime.world;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

public class World
{
    public static long buildDescription(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4)
    {
        int x1i = ((int) (x1 * 255)) & 0xFF;
        int y1i = ((int) (y1 * 255)) & 0xFF;
        int x2i = ((int) (x2 * 255)) & 0xFF;
        int y2i = ((int) (y2 * 255)) & 0xFF;
        int x3i = ((int) (x3 * 255)) & 0xFF;
        int y3i = ((int) (y3 * 255)) & 0xFF;
        int x4i = ((int) (x4 * 255)) & 0xFF;
        int y4i = ((int) (y4 * 255)) & 0xFF;
        return (x1i << 56) | (y1i << 48) | (x2i << 40) | (y2i << 32) | (x3i << 24) | (y3i << 16) | (x4i << 8) | (y4i);
    }   
    
    public static final long DESC_FULLBLOCK = buildDescription(0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f);
    public static final long DESC_BOTTOMSLAB = buildDescription(0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.5f, 0.0f, 0.5f);
    public static final long DESC_TOPSLAB = buildDescription(0.0f, 0.5f, 1.0f, 0.5f, 1.0f, 1.0f, 0.0f, 1.0f);
    public static final long DESC_LEFTPILLAR = buildDescription(0.0f, 0.0f, 0.5f, 0.0f, 0.5f, 1.0f, 0.0f, 1.0f);
    public static final long DESC_RIGHTPILLAR = buildDescription(0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.5f, 1.0f);
    public static final long DESC_SLOPELEFT = buildDescription(0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f);
    public static final long DESC_SLOPERIGHT = buildDescription(0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f);
    public static final long DESC_SLOPELEFT_YFLIP = buildDescription(0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    public static final long DESC_SLOPERIGHT_YFLIP = buildDescription(0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f);
    
    private int width;
    private int height;
    
    private Map<Short, Material> materialMap;
    
    private short[] tileMaterial;
    private long[] tileDescription;
    
    private int tileDisplayList = Integer.MAX_VALUE;
    
    public World(int width, int height)
    {
        this.width = width;
        this.height = height;
        
        materialMap = new HashMap<Short, Material>();
        
        tileMaterial = new short[width * height];
        tileDescription = new long[width * height];
    }
    
    public void bindMaterialToID(short id, Material material)
    {
        materialMap.put(id, material);
    }
    
    public void setTileMaterial(int x, int y, short mat)
    {
        tileMaterial[y * width + x] = mat;
    }
    
    public void setTileDescription(int x, int y, long desc)
    {
        tileDescription[y * width + x] = desc;
    }
    
    public void update(float timeDelta)
    {
        
    }
    
    private void generateTileDisplayList()
    {
        tileDisplayList = GL11.glGenLists(1);
        GL11.glNewList(tileDisplayList, GL11.GL_COMPILE);
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        for(int y = 0; y < height; y++)
            for(int x = 0; x < width; x++)
                if(materialMap.get(tileMaterial[y * width + x]).TILE_POINT_COUNT == 4)
                {
                    long desc = tileDescription[y * width + x];
                    float x1 = ((desc >> 56) & 0xFF) / 255.0f;
                    float y1 = ((desc >> 48) & 0xFF) / 255.0f;
                    float x2 = ((desc >> 40) & 0xFF) / 255.0f;
                    float y2 = ((desc >> 32) & 0xFF) / 255.0f;
                    float x3 = ((desc >> 24) & 0xFF) / 255.0f;
                    float y3 = ((desc >> 16) & 0xFF) / 255.0f;
                    float x4 = ((desc >> 8) & 0xFF) / 255.0f;
                    float y4 = ((desc) & 0xFF) / 255.0f;
                    GL11.glTexCoord2f(x1, 1.0f - y1);
                    GL11.glVertex2f(x1, y1);
                    GL11.glTexCoord2f(x2, 1.0f - y2);
                    GL11.glVertex2f(x2, y2);
                    GL11.glTexCoord2f(x3, 1.0f - y3);
                    GL11.glVertex2f(x3, y3);
                    GL11.glTexCoord2f(x4, 1.0f - y4);
                    GL11.glVertex2f(x4, y4);
                }
        GL11.glEnd();
        GL11.glColor3f(1.0f, 0.0f, 0.0f);
        GL11.glBegin(GL11.GL_TRIANGLES);
        for(int y = 0; y < height; y++)
            for(int x = 0; x < width; x++)
                if(materialMap.get(tileMaterial[y * width + x]).TILE_POINT_COUNT == 3)
                {
                    long desc = tileDescription[y * width + x];
                    float x1 = ((desc >> 56) & 0xFF) / 255.0f;
                    float y1 = ((desc >> 48) & 0xFF) / 255.0f;
                    float x2 = ((desc >> 40) & 0xFF) / 255.0f;
                    float y2 = ((desc >> 32) & 0xFF) / 255.0f;
                    float x3 = ((desc >> 24) & 0xFF) / 255.0f;
                    float y3 = ((desc >> 16) & 0xFF) / 255.0f;
                    GL11.glTexCoord2f(x1, 1.0f - y1);
                    GL11.glVertex2f(x1, y1);
                    GL11.glTexCoord2f(x2, 1.0f - y2);
                    GL11.glVertex2f(x2, y2);
                    GL11.glTexCoord2f(x3, 1.0f - y3);
                    GL11.glVertex2f(x3, y3);
                }
        GL11.glEnd();
        GL11.glEndList();
    }
    
    public void render()
    {
        if(tileDisplayList == Integer.MAX_VALUE)
            generateTileDisplayList();
        GL11.glCallList(tileDisplayList);
    }
}
