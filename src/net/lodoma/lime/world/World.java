package net.lodoma.lime.world;

import java.io.File;
import java.io.IOException;

import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.script.LuaScript;
import net.lodoma.lime.script.library.AttributeFunctions;
import net.lodoma.lime.script.library.CameraFunctions;
import net.lodoma.lime.script.library.EventFunctions;
import net.lodoma.lime.script.library.InputFunctions;
import net.lodoma.lime.script.library.LightFunctions;
import net.lodoma.lime.script.library.LimeLibrary;
import net.lodoma.lime.script.library.PhysicsFunctions;
import net.lodoma.lime.script.library.UtilFunctions;
import net.lodoma.lime.script.library.WorldFunctions;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.shader.light.Light;
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
        CameraFunctions.addToLibrary(library);
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
    
    public void applySnapshot(SnapshotSegment segment, Client client)
    {
        synchronized (lock)
        {
            // Remove components and lights
            
            for (int key : segment.removedComponents)
            {
                compoSnapshotPool.remove(key);
            }
            
            for (int key : segment.removedLights)
            {
                lightPool.get(key).destroy();
                lightPool.remove(key);
            }
            
            // Create components and lights
            
            for (int key : segment.createdComponents)
            {
                PhysicsComponentSnapshot compo = new PhysicsComponentSnapshot();
                compo.identifier = key;
                compoSnapshotPool.addManaged(compo);
            }
            
            for (int key : segment.createdLights)
            {
                Light light = new Light(this);
                light.identifier = key;
                lightPool.addManaged(light);
            }
            
            // Modify components and lights
            
            for (int i = 0; i < segment.modifiedComponents.length; i++)
            {
                int key = (int) (segment.modifiedComponents[i] & 0xFFFFFFFF);
                segment.productComponents[i].apply(compoSnapshotPool.get(key));
            }
            
            for (int i = 0; i < segment.modifiedLights.length; i++)
            {
                int key = (int) (segment.modifiedLights[i] & 0xFFFFFFFF);
                segment.productLights[i].apply(lightPool.get(key).data);
            }
        }
    }
}
