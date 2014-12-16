package net.lodoma.lime.world;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.Consumer;

import net.lodoma.lime.common.NetworkSide;
import net.lodoma.lime.common.PropertyPool;
import net.lodoma.lime.physics.Entity;
import net.lodoma.lime.physics.EntityFactory;
import net.lodoma.lime.physics.EntityFactoryException;
import net.lodoma.lime.physics.EntityLoader;
import net.lodoma.lime.physics.EntityLoaderException;
import net.lodoma.lime.physics.PhysicsWorld;
import net.lodoma.lime.physics.VisualWorld;
import net.lodoma.lime.script.LuaScript;
import net.lodoma.lime.shader.light.Light;
import net.lodoma.lime.util.IdentityPool;
import net.lodoma.lime.util.HashPool32;

public abstract class CommonWorld
{
    public String name;
    public String version;
    
    public LuaScript script;

    public PhysicsWorld physicsWorld;
    public VisualWorld visualWorld;
    
    public HashPool32<EntityFactory> entityFactoryPool;
    public IdentityPool<Entity> entityPool;
    
    public IdentityPool<Light> lightPool;
    
    public CommonWorld()
    {
        physicsWorld = new PhysicsWorld();
        visualWorld = null;
        
        entityFactoryPool = new HashPool32<EntityFactory>();
        entityPool = new IdentityPool<>();
        
        lightPool = new IdentityPool<Light>();
    }
    
    public void load()
    {
        try
        {
            File[] files = new File("./model").listFiles();
            for(File file : files)
                if(file.isFile() && file.getName().toLowerCase().endsWith(".xml"))
                {
                    EntityFactory factory = EntityLoader.loadEntityType(new FileInputStream(file), this);
                    entityFactoryPool.add(factory.nameHash, factory);
                    
                    System.out.printf("Created EntityFactory for %s\n", factory.name);
                }
        }
        catch(EntityLoaderException | IOException e)
        {
            // TODO: handle this later
            e.printStackTrace();
        }
    }
    
    public abstract NetworkSide getNetworkSide();
    
    public abstract PropertyPool getPropertyPool();
    
    public int newEntity(int hash)
    {
        try
        {
            EntityFactory factory = entityFactoryPool.get(hash);
            Entity entity = factory.newEntity();
            entityPool.add(entity);
            entity.initialize();
            
            int entityID = entity.getIdentifier();
            System.out.printf("Created entity %s[%d]\n", factory.name, entityID);
            return entityID;
        }
        catch (EntityFactoryException e)
        {
            // TODO: handle this later
            e.printStackTrace();
            return -1;
        }
    }
    
    // This function exists so ClientsideWorld and ServersideWorld can override it
    public int newLight(Light light)
    {
        return lightPool.add(light);
    }
    
    public void clean()
    {
        // NOTE: The Consumer here should probably be in a final field
        entityPool.foreach(new Consumer<Entity>()
        {
            @Override
            public void accept(Entity entity)
            {
                entity.destroy();
                entityPool.remove(entity);
            }
        });
        
        entityFactoryPool.foreach(new Consumer<EntityFactory>()
        {
            @Override
            public void accept(EntityFactory factory)
            {
                factory.destroy();
                entityFactoryPool.remove(factory.nameHash);
            }
        });
    }
    
    public void update(double timeDelta)
    {
        if(script != null)
        {
            Object[] arguments = new Object[] { timeDelta };
            script.call("Lime_Update", arguments);
        }
        
        entityPool.foreach(new Consumer<Entity>()
        {
            @Override
            public void accept(Entity entity)
            {
                entity.update((float) timeDelta);
            }
        });
        
        physicsWorld.update((float) timeDelta);
        
        entityPool.foreach(new Consumer<Entity>()
        {
            @Override
            public void accept(Entity entity)
            {
                entity.postUpdate((float) timeDelta);
            }
        });
    }
}
