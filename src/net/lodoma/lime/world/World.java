package net.lodoma.lime.world;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.Map.Entry;

import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;

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
import net.lodoma.lime.world.physics.PhysicsComponent;
import net.lodoma.lime.world.physics.PhysicsComponentSnapshot;
import net.lodoma.lime.world.physics.PhysicsJoint;

public class World
{
    public LuaScript luaInstance;
    public LuaFunction gamemodeWorldInit;
    public LuaFunction gamemodeUpdate;
    
    public Object lock = new Object();
    public IdentityPool<Entity> entityPool;
    public IdentityPool<PhysicsComponent> componentPool;
    public IdentityPool<PhysicsComponentSnapshot> compoSnapshotPool;
    public IdentityPool<PhysicsJoint> jointPool;
    public IdentityPool<Light> lightPool;
    
    public World()
    {
        entityPool = new IdentityPool<Entity>(false);
        componentPool = new IdentityPool<PhysicsComponent>(false);
        compoSnapshotPool = new IdentityPool<PhysicsComponentSnapshot>(false);
        jointPool = new IdentityPool<PhysicsJoint>(false);
        lightPool = new IdentityPool<Light>(false);
    }
    
    public void clean()
    {
        entityPool.foreach((Entity entity) -> {
            entity.destroy();
            entityPool.remove(entity);
        });
        entityPool.clear();
        
        componentPool.foreach((PhysicsComponent component) -> component.destroy());
        componentPool.clear();
        
        jointPool.foreach((PhysicsJoint joint) -> joint.destroy());
        jointPool.clear();
        
        lightPool.foreach((Light light) -> light.destroy());
        lightPool.clear();
    }
    
    public void load(String filepath, Server server) throws IOException
    {
        LimeLibrary library = new LimeLibrary(server);
        AttributeFunctions.addToLibrary(library);
        EventFunctions.addToLibrary(library);
        InputFunctions.addToLibrary(library);
        LightFunctions.addToLibrary(library);
        PhysicsFunctions.addToLibrary(library);
        UtilFunctions.addToLibrary(library);
        WorldFunctions.addToLibrary(library);
        luaInstance = new LuaScript(library);
        
        luaInstance.load(new File(OsHelper.JARPATH + "script/world/" + filepath + ".lua"));
        
        gamemodeWorldInit = luaInstance.globals.get("Lime_WorldInit").checkfunction();
        gamemodeUpdate = luaInstance.globals.get("Lime_Update").checkfunction();

        luaInstance.globals.set("Lime_WorldInit", LuaValue.NIL);
        luaInstance.globals.set("Lime_Update", LuaValue.NIL);
    }
    
    public void init()
    {
        luaInstance.call(gamemodeWorldInit, null);
    }
    
    public void updateGamemode(double timeDelta)
    {
        luaInstance.call(gamemodeUpdate, new Object[] { timeDelta });
    }
    
    public void updateEntities(double timeDelta)
    {
        entityPool.foreach((Entity entity) -> { entity.update(timeDelta); });
    }
    
    public void applySnapshot(Snapshot snapshot, Client client)
    {
        synchronized (lock)
        {
            if (snapshot.isDelta)
            {
                for (Integer identifier : snapshot.removedComponents)
                    compoSnapshotPool.remove(identifier);
                for (Integer identifier : snapshot.removedLights)
                {
                    lightPool.get(identifier).destroy();
                    lightPool.remove(identifier);
                }
            }
            else
            {
                Set<Integer> componentIdentifierSet = compoSnapshotPool.getIdentifierSet();
                for (Integer identifier : componentIdentifierSet)
                    if (!snapshot.componentData.containsKey(identifier))
                        compoSnapshotPool.remove(identifier);
                
                Set<Integer> lightIdentifierSet = lightPool.getIdentifierSet();
                for (Integer identifier : lightIdentifierSet)
                    if (!snapshot.lightData.containsKey(identifier))
                    {
                        lightPool.get(identifier).destroy();
                        lightPool.remove(identifier);
                    }
            }
            
            Set<Integer> lightKeySet = snapshot.lightData.keySet();
            for (Integer identifier : lightKeySet)
                if (!lightPool.has(identifier))
                {
                    Light light = new Light(this);
                    light.identifier = identifier;
                    lightPool.addManaged(light);
                }
    
            Set<Entry<Integer, PhysicsComponentSnapshot>> componentEntrySet = snapshot.componentData.entrySet();
            for (Entry<Integer, PhysicsComponentSnapshot> entry : componentEntrySet)
            {
                int identifier = entry.getKey();
                PhysicsComponentSnapshot compoSnapshot = entry.getValue();
                compoSnapshot.identifier = identifier;
                if (compoSnapshotPool.has(identifier))
                    compoSnapshotPool.remove(identifier);
                compoSnapshotPool.addManaged(compoSnapshot);
            }
    
            Set<Entry<Integer, LightData>> lightEntrySet = snapshot.lightData.entrySet();
            for (Entry<Integer, LightData> entry : lightEntrySet)
                lightPool.get(entry.getKey()).data = entry.getValue();
        }
    }
}
