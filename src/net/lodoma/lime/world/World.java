package net.lodoma.lime.world;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.script.LuaScript;
import net.lodoma.lime.script.library.EntityFunctions;
import net.lodoma.lime.script.library.LimeLibrary;
import net.lodoma.lime.script.library.UtilFunctions;
import net.lodoma.lime.script.library.WorldFunctions;
import net.lodoma.lime.server.Server;
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
        entityPool.clear();
        entityTypePool.clear();
    }
    
    public void load(String filepath, Server server) throws IOException
    {
        LimeLibrary library = new LimeLibrary(server);
        UtilFunctions.addToLibrary(library);
        WorldFunctions.addToLibrary(library);
        EntityFunctions.addToLibrary(library);
        
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
        entityPool.foreach(new Consumer<Entity>()
        {
            @Override
            public void accept(Entity entity)
            {
                entity.update(timeDelta); 
            }
        });
    }
    
    public void acceptSnapshot(ByteBuffer snapshot, Client client)
    {
        snapshot.position(0);
        int snapshotCompoc = snapshot.getInt();
        for (int i = 0; i < snapshotCompoc; i++)
        {
            int entityID = snapshot.getInt();
            if (!entityPool.has(entityID))
            {
                Entity entity = new Entity(this, entityID, client);
                // NOTE: The entity here is weirdly added to the pool because entityPool isn't managed.
                entityPool.addManaged(entity);
            }
            entityPool.get(entityID).acceptSnapshotCompo(snapshot);
        }
    }
    
    public ByteBuffer buildSnapshot(boolean forced)
    {
        List<byte[]> snapshotCompos = new ArrayList<byte[]>();
        
        entityPool.foreach(new Consumer<Entity>()
        {
            @Override
            public void accept(Entity entity)
            {
                byte[] compo = entity.buildSnapshotCompo(forced);
                if (compo != null)
                    snapshotCompos.add(compo);
            }
        });
        
        int snapshotSize = 0;
        for (byte[] snapshotCompo : snapshotCompos)
            snapshotSize += snapshotCompo.length;
        
        ByteBuffer snapshot = ByteBuffer.allocate(snapshotSize + 4);
        snapshot.putInt(snapshotCompos.size());
        for (byte[] snapshotCompo : snapshotCompos)
            snapshot.put(snapshotCompo);
        
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
