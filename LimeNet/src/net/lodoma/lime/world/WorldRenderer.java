package net.lodoma.lime.world;

import net.lodoma.lime.texture.TexturePool;
import net.lodoma.lime.world.material.Material;

import org.lwjgl.opengl.GL11;

public class WorldRenderer
{
    private ClientsideWorld world;
    
    public WorldRenderer(ClientsideWorld world)
    {
        this.world = world;
    }
    
    public void render()
    {
        // TODO: use display lists
        
        // these values could be fields
        // but getting them every frame makes the system more dynamic
        // if this becomes an issue, it will get fixed
        int width = world.getWidth();
        int height = world.getHeight();
        TexturePool texturePool = world.getTexturePool();
        
        for(int y = 0; y < height; y++)
            for(int x = 0; x < width; x++)
            {
                short tileMaterial = world.getTileMaterial(x, y);
                Material material = world.getPaletteMaterial(tileMaterial);
                
                if(material == null) continue;
                
                if(!material.rendered) continue;
                if(material.texture != 0)
                    texturePool.getTexture(material.texture).bind(0);
                
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
}
