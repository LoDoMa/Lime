package net.lodoma.lime.world.client;

import java.util.List;

import net.lodoma.lime.physics.entity.Entity;
import net.lodoma.lime.world.platform.Platform;

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
        GL11.glPushMatrix();
        GL11.glScalef(1.0f / 32.0f, 1.0f / 24.0f, 1.0f);
        
        List<Platform> platformList = world.getPlatformList();
        for(Platform platform : platformList)
            platform.render();
        
        List<Entity> entityList = world.getEntityList();
        for(Entity entity : entityList)
            entity.render();
        
        GL11.glPopMatrix();
    }
}
