package net.lodoma.lime.world.builder;

import java.io.File;

import net.lodoma.lime.physics.entity.Entity;
import net.lodoma.lime.physics.entity.EntityLoader;
import net.lodoma.lime.util.Vector2;
import net.lodoma.lime.world.platform.Platform;
import net.lodoma.lime.world.server.ServersideWorld;

public class WorldFileLoader implements WorldBuilder
{
    @Override
    public void build(ServersideWorld world)
    {
        try
        {
            EntityLoader entityLoader = (EntityLoader) world.getServer().getProperty("entityLoader");
            
            Entity entity = entityLoader.loadFromXML(new File("model/zombie.xml"), world);
            world.addEntity(entity);
            
            Platform platform = new Platform(new Vector2(0, 0),
                    new Vector2(-10, 0),
                    new Vector2(10, 0),
                    new Vector2(10, -5),
                    new Vector2(-10, -5));
            world.addPlatform(platform);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
