package net.lodoma.lime.world;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;

import net.lodoma.lime.script.LuaScript;
import net.lodoma.lime.script.library.AttributeFunctions;
import net.lodoma.lime.script.library.CameraFunctions;
import net.lodoma.lime.script.library.EventFunctions;
import net.lodoma.lime.script.library.InputFunctions;
import net.lodoma.lime.script.library.LightFunctions;
import net.lodoma.lime.script.library.LimeLibrary;
import net.lodoma.lime.script.library.ParticleFunctions;
import net.lodoma.lime.script.library.PhysicsFunctions;
import net.lodoma.lime.script.library.UtilFunctions;
import net.lodoma.lime.script.library.WorldFunctions;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.shader.light.Light;
import net.lodoma.lime.util.Color;
import net.lodoma.lime.util.IdentityPool;
import net.lodoma.lime.util.OsHelper;
import net.lodoma.lime.world.entity.Entity;
import net.lodoma.lime.world.physics.PhysicsComponent;
import net.lodoma.lime.world.physics.PhysicsComponentSnapshot;
import net.lodoma.lime.world.physics.PhysicsJoint;
import net.lodoma.lime.world.physics.PhysicsParticle;
import net.lodoma.lime.world.physics.PhysicsParticleDefinition;
import net.lodoma.lime.world.physics.PhysicsWorld;

public class World
{
    public PhysicsWorld physicsWorld;
    
    public LuaScript luaInstance;
    public LuaFunction gamemodeWorldInit;
    public LuaFunction gamemodeInit;
    public LuaFunction gamemodeUpdate;
    public LuaFunction gamemodePostUpdate;
    public LuaFunction gamemodeClean;
    
    public final Object lock = new Object();
    public final IdentityPool<Entity> entityPool;
    public final IdentityPool<PhysicsComponent> componentPool;
    public final IdentityPool<PhysicsComponentSnapshot> compoSnapshotPool;
    public final IdentityPool<PhysicsJoint> jointPool;
    public final List<PhysicsParticle> particleList;
    public final List<PhysicsParticleDefinition> particleDefinitionList;
    public final IdentityPool<Light> lightPool;
    public final Color lightAmbientColor;
    
    public String gamemode;
    
    public World()
    {
        physicsWorld = new PhysicsWorld();
        
        entityPool = new IdentityPool<Entity>(false);
        componentPool = new IdentityPool<PhysicsComponent>(false);
        compoSnapshotPool = new IdentityPool<PhysicsComponentSnapshot>(false);
        jointPool = new IdentityPool<PhysicsJoint>(false);
        particleList = new ArrayList<PhysicsParticle>();
        particleDefinitionList = new ArrayList<PhysicsParticleDefinition>();
        lightPool = new IdentityPool<Light>(false);
        lightAmbientColor = new Color(0.0f, 0.0f, 0.0f);
    }
    
    public void clean()
    {
        if (luaInstance != null)
            luaInstance.call(gamemodeClean, null);
        
        entityPool.foreach((Entity entity) -> entity.destroy());
        entityPool.clear();
        
        componentPool.foreach((PhysicsComponent component) -> component.destroy());
        componentPool.clear();
        
        compoSnapshotPool.foreach((PhysicsComponentSnapshot compoSnapshot) -> compoSnapshot.destroy());
        compoSnapshotPool.clear();
        
        jointPool.foreach((PhysicsJoint joint) -> joint.destroy());
        jointPool.clear();
        
        particleList.forEach((PhysicsParticle particle) -> particle.destroy());
        particleList.clear();
        particleDefinitionList.clear();
        
        lightPool.foreach((Light light) -> light.destroy());
        lightPool.clear();
        
        physicsWorld.destroy();
        
        Light.destroyFBOs();
    }
    
    public void load(String filepath, Server server) throws IOException
    {
        LimeLibrary library = new LimeLibrary(server);
        AttributeFunctions.addToLibrary(library);
        CameraFunctions.addToLibrary(library);
        EventFunctions.addToLibrary(library);
        InputFunctions.addToLibrary(library);
        LightFunctions.addToLibrary(library);
        ParticleFunctions.addToLibrary(library);
        PhysicsFunctions.addToLibrary(library);
        UtilFunctions.addToLibrary(library);
        WorldFunctions.addToLibrary(library);
        luaInstance = new LuaScript(library);
        
        luaInstance.load(new File(OsHelper.JARPATH + "script/world/" + filepath + ".lua"));

        gamemodeWorldInit = luaInstance.globals.get("Lime_WorldInit").checkfunction();
        gamemodeInit = luaInstance.globals.get("Lime_Init").checkfunction();
        gamemodeUpdate = luaInstance.globals.get("Lime_Update").checkfunction();
        gamemodePostUpdate = luaInstance.globals.get("Lime_PostUpdate").checkfunction();
        gamemodeClean = luaInstance.globals.get("Lime_Clean").checkfunction();

        luaInstance.globals.set("Lime_WorldInit", LuaValue.NIL);
        luaInstance.globals.set("Lime_Init", LuaValue.NIL);
        luaInstance.globals.set("Lime_Update", LuaValue.NIL);
        luaInstance.globals.set("Lime_PostUpdate", LuaValue.NIL);
        luaInstance.globals.set("Lime_Clean", LuaValue.NIL);
    }
    
    public void init()
    {
        luaInstance.call(gamemodeWorldInit, null);
        physicsWorld.create();
        
        luaInstance.call(gamemodeInit, null);
    }
    
    public void updateGamemode(double timeDelta)
    {
        luaInstance.call(gamemodeUpdate, new Object[] { timeDelta });
    }
    
    public void updateEntities(double timeDelta)
    {
        entityPool.foreach((Entity entity) -> entity.update(timeDelta));
    }
    
    public void postUpdateGamemode()
    {
        luaInstance.call(gamemodePostUpdate, new Object[] { });
    }
    
    public void postUpdateEntities()
    {
        entityPool.foreach((Entity entity) -> entity.postUpdate());
    }
    
    public void updateParticles(double timeDelta)
    {
        synchronized (lock)
        {
            Iterator<PhysicsParticle> it = particleList.iterator();
            while (it.hasNext())
            {
                PhysicsParticle particle = it.next();
                particle.update(timeDelta);
                if (particle.destroyed)
                    it.remove();
            }
        }
    }
}
