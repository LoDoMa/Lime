package net.lodoma.lime.world;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import net.lodoma.lime.common.NetworkSide;
import net.lodoma.lime.common.PropertyPool;
import net.lodoma.lime.physics.PhysicsWorld;
import net.lodoma.lime.physics.entity.Entity;
import net.lodoma.lime.physics.entity.EntityLoader;
import net.lodoma.lime.physics.entity.EntityLoaderException;
import net.lodoma.lime.physics.entity.EntityPool;
import net.lodoma.lime.physics.entity.EntityType;
import net.lodoma.lime.physics.entity.EntityTypePool;
import net.lodoma.lime.script.LuaScript;

public abstract class CommonWorld
{
    protected String name;
    protected String version;
    
    protected LuaScript script;
    
    protected PhysicsWorld physicsWorld;
    
    protected EntityPool entityPool;
    protected EntityTypePool entityTypes;
    
    public CommonWorld()
    {
        physicsWorld = new PhysicsWorld();
        
        entityPool = new EntityPool();
        entityTypes = new EntityTypePool();
    }
    
    public void load()
    {
        try
        {
            File[] files = new File("./model").listFiles();
            for(File file : files)
                if(file.isFile() && file.getName().toLowerCase().endsWith(".xml"))
                {
                    EntityType type = EntityLoader.loadEntity(new FileInputStream(file), this);
                    entityTypes.add(type.getNameHash(), type);
                    
                    System.out.printf("Loaded EntityType %s\n", type.getName());
                }
        }
        catch(EntityLoaderException | IOException e)
        {
            e.printStackTrace();
            // TODO: handle this later
        }
    }
    
    public abstract NetworkSide getNetworkSide();
    
    public abstract PropertyPool getPropertyPool();
    
    public PhysicsWorld getPhysicsWorld()
    {
        return physicsWorld; 
    }
    
    public int newEntity(int hash)
    {
        EntityType type = entityTypes.get(hash);
        Entity entity = type.newEntity();
        entityPool.add(entity);
        return entity.getIdentifier();
    }
    
    public Entity getEntity(int id)
    {
        return entityPool.get(id);
    }
    
    public void removeEntity(int id)
    {
        entityPool.remove(id);
    }
    
    public Set<Integer> getEntityIDSet()
    {
        return entityPool.getIdentifierSet();
    }
    
    public List<Entity> getEntityList()
    {
        return entityPool.getObjectList();
    }
    
    public void clean()
    {
        List<Entity> entityList = getEntityList();
        for(Entity entity : entityList)
        {
            entity.destroy();
            entityPool.remove(entity.getIdentifier());
        }
    }
    
    public void update(double timeDelta)
    {
        if(script != null)
            script.call("Lime_WorldUpdate", null);
        
        List<Entity> entityList = getEntityList();
        for(Entity entity : entityList)
            entity.update(timeDelta);
        
        physicsWorld.update(timeDelta);
    }
}
