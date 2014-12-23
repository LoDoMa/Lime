package net.lodoma.lime.world;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.function.Consumer;

import net.lodoma.lime.script.LimeLibrary;
import net.lodoma.lime.script.LuaScript;
import net.lodoma.lime.util.IdentityPool;
import net.lodoma.lime.world.entity.Entity;
import net.lodoma.lime.world.entity.EntityType;

public class World
{
    public LuaScript gamemode;
    public IdentityPool<EntityType> entityTypePool;
    public IdentityPool<Entity> entityPool;
    
    public World()
    {
        entityTypePool = new IdentityPool<EntityType>(true);
        entityPool = new IdentityPool<Entity>(false);
    }
    
    public void clean()
    {
        entityPool.foreach(new Consumer<Entity>()
        {
            @Override
            public void accept(Entity entity)
            {
                entity.destroy();
                entityPool.remove(entity);
            }
        });
    }
    
    public void load(String filepath) throws IOException
    {
        File[] entityTypeFiles = new File("./types").listFiles();
        for (File entityTypeFile : entityTypeFiles)
        {
            if (!entityTypeFile.isFile()) continue;
            EntityType entityType = new EntityType(entityTypeFile);
            entityTypePool.add(entityType);
        }
        
        gamemode = new LuaScript(new LimeLibrary());
        gamemode.load(new File("./script/world/" + filepath + ".lua"));
    }
    
    public void updateGamemode(double timeDelta)
    {
        gamemode.call("Lime_Update", new Object[] { timeDelta });
    }
    
    public void acceptSnapshot(ByteBuffer snapshot)
    {
        System.out.println("I accept!");
    }
    
    public ByteBuffer buildFullSnapshot()
    {
        ByteBuffer snapshot = ByteBuffer.allocate(0);
        return snapshot;
    }
    
    public ByteBuffer buildDeltaSnapshot()
    {
        ByteBuffer snapshot = ByteBuffer.allocate(0);
        return snapshot;
    }
    
    public void snapshotUpdate()
    {
        entityPool.foreach(new Consumer<Entity>()
        {
            @Override
            public void accept(Entity entity)
            {
                // New is outdated!
            }
        });
    }
}
