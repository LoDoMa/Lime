package net.lodoma.lime.world.builder;

import java.io.File;

import net.lodoma.lime.event.EventManager;
import net.lodoma.lime.physics.entity.Entity;
import net.lodoma.lime.physics.entity.EntityLoader;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.util.HashPool32;
import net.lodoma.lime.util.Vector2;
import net.lodoma.lime.world.platform.Platform;
import net.lodoma.lime.world.server.ServersideWorld;

public class WorldFileLoader implements WorldBuilder
{
    @SuppressWarnings("unchecked")
    @Override
    public void build(ServersideWorld world)
    {
        try
        {
            Server server = world.getServer();
            EntityLoader entityLoader = (EntityLoader) server.getProperty("entityLoader");
            HashPool32<EventManager> emanPool = (HashPool32<EventManager>) server.getProperty("emanPool");
            
            Entity entity = entityLoader.loadFromXML(new File("model/zombie.xml"), world, emanPool);
            world.addEntity(entity);
            
            Platform platform = new Platform(new Vector2(0, 0),
                    new Vector2(-10, 0.5f),
                    new Vector2(10, 0.5f),
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
