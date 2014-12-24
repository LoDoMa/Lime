package net.lodoma.lime.world.entity;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Random;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.script.LuaScript;
import net.lodoma.lime.script.library.LimeLibrary;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.util.Vector2;
import net.lodoma.lime.world.World;
import net.lodoma.lime.world.entity.physics.IntersectionEvent;
import net.lodoma.lime.world.entity.physics.PhysicsUtils;

public class Entity implements Identifiable<Integer>
{
    public int identifier;
    
    public LuaScript script;
    public World world;
    
    public Body body = new Body();
    
    public boolean skipSimulation = false;
    
    public Entity(World world, int hash, Server server)
    {
        this.world = world;
        
        EntityType type = world.entityTypePool.get(hash);
        script = new LuaScript(new LimeLibrary(server));
        
        try
        {
            script.load(new File("./script/entity/" + type.script + ".lua"));       // load entity script
        }
        catch (IOException e)
        {
            // TODO: handle this
            e.printStackTrace();
        }
        
        // TODO: delete temp test code
        Random random = new Random();
        body.velocity = new Vector2((random.nextFloat() * 2.0f - 1.0f) * 1.0f, (random.nextFloat() * 2.0f - 1.0f) * 1.0f);
        body.position = new Vector2(random.nextFloat() * 32.0f, random.nextFloat() * 25.0f);
        body.radius = 0.5f + random.nextFloat() * 0.5f;
        body.density = 1.0f;
        body.mass = body.density * (float) ((4.0 / 3.0) * Math.PI * (body.radius * body.radius * body.radius));
    }
    
    public Entity(World world, int identifier, Client client)
    {
        this.identifier = identifier;
    }
    
    @Override
    public Integer getIdentifier()
    {
        return identifier;
    }
    
    @Override
    public void setIdentifier(Integer identifier)
    {
        this.identifier = identifier;
    }
    
    public void initialize()
    {
        
    }
    
    public void destroy()
    {
        
    }
    
    public void update(float timeDelta)
    {
        script.call("Lime_Update", new Object[] { identifier, timeDelta, false });
    }
    
    public void debugRender()
    {
        body.debugRender();
    }
    
    public void render()
    {
        throw new UnsupportedOperationException();
    }
    
    public boolean checkSpawnConditions()
    {
        List<Entity> entities = world.entityPool.getObjectList();
        for (Entity other : entities)
        {
            float dx = body.position.x - other.body.position.x;
            float dy = body.position.y - other.body.position.y;
            float dist = (float) Math.sqrt(dx * dx + dy * dy);
            if (dist < (body.radius + other.body.radius))
                return false;
        }
        return true;
    }
    
    public IntersectionEvent intersects(Entity other)
    {
        double t = PhysicsUtils.getIntersectionTime(body.position,       body.velocity,       body.radius,
                                                    other.body.position, other.body.velocity, other.body.radius);
        
        IntersectionEvent event = null;
        if (t >= 0.0)
        {
            event = new IntersectionEvent();
            event.time = t;
            event.entityID1 = identifier;
            event.entityID2 = other.identifier;
        }
        return event;
    }
    
    public void simulate(float timeDelta)
    {
        body.position.addLocal(body.velocity.mul(timeDelta));
    }
    
    public void collideWith(Entity other)
    {
        PhysicsUtils.getPostCollisionVelocity(body.position,       body.velocity,       body.mass,
                                              other.body.position, other.body.velocity, other.body.mass,
                                              body.velocity, other.body.velocity);
    }
    
    public void acceptSnapshotCompo(ByteBuffer compo)
    {
        body.position.x = compo.getFloat();
        body.position.y = compo.getFloat();
        body.velocity.x = compo.getFloat();
        body.velocity.y = compo.getFloat();
        body.radius = compo.getFloat();
    }
    
    public byte[] buildSnapshotCompo(boolean forced)
    {
        if (!forced && !body.snapshotUpdate())
            return null;
        
        ByteBuffer compo = ByteBuffer.allocate(24);
        compo.putInt(identifier);
        
        compo.putFloat(body.position.x);
        compo.putFloat(body.position.y);
        compo.putFloat(body.velocity.x);
        compo.putFloat(body.velocity.y);
        compo.putFloat(body.radius);
        
        return compo.array();
    }
}
