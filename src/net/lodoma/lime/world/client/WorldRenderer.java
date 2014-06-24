package net.lodoma.lime.world.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.lodoma.lime.texture.TexturePool;
import net.lodoma.lime.world.material.Material;

import org.lwjgl.opengl.GL11;

public class WorldRenderer
{
    private ClientsideWorld world;
    private TexturePool texturePool;
    
    /*
     * Tiles using the same Material have exactly the same rendering properties.
     * There is one display list for each Material type in the world,
     * containing vertices for all tiles using that material.
     */
    private Map<Material, Integer> displayLists;
    
    public WorldRenderer(ClientsideWorld world)
    {
        this.world = world;
        displayLists = new HashMap<Material, Integer>();
    }
    
    public void recompile()
    {
        List<Integer> previousDisplayLists = new ArrayList<Integer>(displayLists.values());
        for(int list : previousDisplayLists)
            GL11.glDeleteLists(list, 1);
        displayLists.clear();
        
        int width = world.getWidth();
        int height = world.getHeight();
        texturePool = world.getTexturePool();
        
        Set<Short> keySet = world.getPaletteKeys();
        for(short key : keySet)
        {
            Material material = world.getPaletteMaterial(key);
            if(!material.rendered) continue;
                
            int displayList = GL11.glGenLists(1);
            GL11.glNewList(displayList, GL11.GL_COMPILE);
            
            boolean usedMaterial = false;
            for(int y = 0; y < height; y++)
                for(int x = 0; x < width; x++)
                {
                    short tileKey = world.getTileMaterial(x, y);
                    if(tileKey == key)
                    {
                        usedMaterial = true;
                        
                        byte tileShape = world.getTileShape(x, y);
                        GL11.glBegin(GL11.GL_POLYGON);
                        if((tileShape & ClientsideWorld.MASK_TILESHAPE_BOTTOM_LEFT) != 0)
                            {GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex2f(x + 0.0f, y + 0.0f);}
                        if((tileShape & ClientsideWorld.MASK_TILESHAPE_BOTTOM_MIDDLE) != 0)
                            {GL11.glTexCoord2f(0.5f, 1.0f); GL11.glVertex2f(x + 0.5f, y + 0.0f);}
                        if((tileShape & ClientsideWorld.MASK_TILESHAPE_BOTTOM_RIGHT) != 0)
                            {GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex2f(x + 1.0f, y + 0.0f);}
                        if((tileShape & ClientsideWorld.MASK_TILESHAPE_MIDDLE_RIGHT) != 0)
                            {GL11.glTexCoord2f(1.0f, 0.5f); GL11.glVertex2f(x + 1.0f, y + 0.5f);}
                        if((tileShape & ClientsideWorld.MASK_TILESHAPE_TOP_RIGHT) != 0)
                            {GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex2f(x + 1.0f, y + 1.0f);}
                        if((tileShape & ClientsideWorld.MASK_TILESHAPE_TOP_MIDDLE) != 0)
                            {GL11.glTexCoord2f(0.5f, 0.0f); GL11.glVertex2f(x + 0.5f, y + 1.0f);}
                        if((tileShape & ClientsideWorld.MASK_TILESHAPE_TOP_LEFT) != 0)
                            {GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex2f(x + 0.0f, y + 1.0f);}
                        if((tileShape & ClientsideWorld.MASK_TILESHAPE_MIDDLE_LEFT) != 0)
                            {GL11.glTexCoord2f(0.0f, 0.5f); GL11.glVertex2f(x + 0.0f, y + 0.5f);}
                        GL11.glEnd();
                    }
                }
            GL11.glEndList();
            
            if(!usedMaterial)
                GL11.glDeleteLists(displayList, 1);
            else
                displayLists.put(material, displayList);
        }
    }
    
    public void render()
    {
        Set<Material> materials = displayLists.keySet();
        for(Material material : materials)
        {
            if(material.texture != 0)
                texturePool.getTexture(material.texture).bind(0);
            
            int displayList = displayLists.get(material);
            GL11.glCallList(displayList);
        }
    }
}
