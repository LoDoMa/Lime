package net.lodoma.lime.world;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.Map.Entry;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.script.LuaScript;
import net.lodoma.lime.script.library.AttributeFunctions;
import net.lodoma.lime.script.library.EventFunctions;
import net.lodoma.lime.script.library.InputFunctions;
import net.lodoma.lime.script.library.LightFunctions;
import net.lodoma.lime.script.library.LimeLibrary;
import net.lodoma.lime.script.library.PhysicsFunctions;
import net.lodoma.lime.script.library.UtilFunctions;
import net.lodoma.lime.script.library.WorldFunctions;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.shader.light.Light;
import net.lodoma.lime.shader.light.LightData;
import net.lodoma.lime.util.IdentityPool;
import net.lodoma.lime.util.OsHelper;
import net.lodoma.lime.world.entity.Entity;
import net.lodoma.lime.world.entity.EntityShape;

public class World
{
    public LuaScript gamemode;
    public IdentityPool<Entity> entityPool;
    public IdentityPool<Light> lightPool;
    public Terrain terrain;
    
    public World()
    {
        entityPool = new IdentityPool<Entity>(false);
        lightPool = new IdentityPool<Light>(false);
        terrain = new Terrain();
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
        PhysicsFunctions.addToLibrary(library);
        EventFunctions.addToLibrary(library);
        LightFunctions.addToLibrary(library);
        InputFunctions.addToLibrary(library);
        AttributeFunctions.addToLibrary(library);
        
        gamemode = new LuaScript(library);
        gamemode.load(new File(OsHelper.JARPATH + "script/world/" + filepath + ".lua"));
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
            for (Integer identifier : snapshot.removedEntities)
            {
                entityPool.get(identifier).destroy();
                entityPool.remove(identifier);
            }
            
            for (Integer identifier : snapshot.removedLights)
                lightPool.remove(identifier);
        }
        else
        {
            Set<Integer> entityIdentifierSet = entityPool.getIdentifierSet();
            for (Integer identifier : entityIdentifierSet)
                if (!snapshot.entityData.containsKey(identifier))
                {
                    entityPool.get(identifier).destroy();
                    entityPool.remove(identifier);
                }

            Set<Integer> lightIdentifierSet = lightPool.getIdentifierSet();
            for (Integer identifier : lightIdentifierSet)
                if (!snapshot.lightData.containsKey(identifier))
                    lightPool.remove(identifier);
        }
        
        Set<Integer> entityKeySet = snapshot.entityData.keySet();
        for (Integer identifier : entityKeySet)
            if (!entityPool.has(identifier))
            {
                Entity entity = new Entity(this, identifier, client);
                entityPool.addManaged(entity);
            }
        
        Set<Integer> lightKeySet = snapshot.lightData.keySet();
        for (Integer identifier : lightKeySet)
            if (!lightPool.has(identifier))
            {
                Light light = new Light();
                light.identifier = identifier;
                lightPool.addManaged(light);
            }

        Set<Entry<Integer, EntityShape>> entityEntrySet = snapshot.entityData.entrySet();
        for (Entry<Integer, EntityShape> entry : entityEntrySet)
            entityPool.get(entry.getKey()).shape = entry.getValue();

        Set<Entry<Integer, LightData>> lightEntrySet = snapshot.lightData.entrySet();
        for (Entry<Integer, LightData> entry : lightEntrySet)
            lightPool.get(entry.getKey()).data = entry.getValue();
    }
}
