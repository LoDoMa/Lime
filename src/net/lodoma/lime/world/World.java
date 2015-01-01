package net.lodoma.lime.world;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.Map.Entry;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.script.LuaScript;
import net.lodoma.lime.script.library.EventFunctions;
import net.lodoma.lime.script.library.LimeLibrary;
import net.lodoma.lime.script.library.UtilFunctions;
import net.lodoma.lime.script.library.WorldFunctions;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.util.IdentityPool;
import net.lodoma.lime.world.entity.Entity;
import net.lodoma.lime.world.entity.EntityShape;

public class World
{
    public LuaScript gamemode;
    public IdentityPool<Entity> entityPool;
    
    public World()
    {
        entityPool = new IdentityPool<Entity>(false);
    }
    
    public void clean()
    {
        entityPool.foreach((Entity entity) -> {
            entity.destroy();
            entityPool.remove(entity);
        });
        entityPool.clear();
    }
    
    public void load(String filepath, Server server) throws IOException
    {
        LimeLibrary library = new LimeLibrary(server);
        UtilFunctions.addToLibrary(library);
        WorldFunctions.addToLibrary(library);
        EventFunctions.addToLibrary(library);
        
        gamemode = new LuaScript(library);
        gamemode.load(new File("./script/world/" + filepath + ".lua"));
    }
    
    public void init()
    {
        gamemode.call("Lime_WorldInit", null);
    }
    
    public void updateGamemode(double timeDelta)
    {
        gamemode.call("Lime_Update", new Object[] { timeDelta });
    }
    
    public void updateEntities(double timeDelta)
    {
        entityPool.foreach((Entity entity) -> { entity.update(timeDelta); });
    }
    
    public void applySnapshot(Snapshot snapshot, Client client)
    {
        if (snapshot.isDelta)
        {
            for (Integer identifier : snapshot.removed)
            {
                entityPool.get(identifier).destroy();
                entityPool.remove(identifier);
            }
        }
        else
        {
            Set<Integer> identifierSet = entityPool.getIdentifierSet();
            for (Integer identifier : identifierSet)
                if (!snapshot.entityData.containsKey(identifier))
                {
                    entityPool.get(identifier).destroy();
                    entityPool.remove(identifier);
                }
        }
        
        Set<Integer> keySet = snapshot.entityData.keySet();
        for (Integer identifier : keySet)
            if (!entityPool.has(identifier))
            {
                Entity entity = new Entity(this, identifier, client);
                entityPool.addManaged(entity);
            }

        Set<Entry<Integer, EntityShape>> entrySet = snapshot.entityData.entrySet();
        for (Entry<Integer, EntityShape> entry : entrySet)
            entityPool.get(entry.getKey()).shape = entry.getValue();
    }
}
