package net.lodoma.lime.world.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientOutput;
import net.lodoma.lime.common.net.NetStage;
import net.lodoma.lime.physics.PhysicsWorld;
import net.lodoma.lime.physics.entity.Entity;
import net.lodoma.lime.util.HashPool;
import net.lodoma.lime.world.TileGrid;
import net.lodoma.lime.world.entity.EntityWorld;
import net.lodoma.lime.world.material.Material;

import org.jbox2d.dynamics.World;

/* Disable formatting
 * @formatter:off
 */
public class ClientsideWorld implements TileGrid, EntityWorld
{
    public static final int MASK_TILESHAPE_BOTTOM_LEFT = 0b00000001;
    public static final int MASK_TILESHAPE_BOTTOM_MIDDLE = 0b00000010;
    public static final int MASK_TILESHAPE_BOTTOM_RIGHT = 0b00000100;
    public static final int MASK_TILESHAPE_MIDDLE_RIGHT = 0b00001000;
    public static final int MASK_TILESHAPE_TOP_RIGHT = 0b00010000;
    public static final int MASK_TILESHAPE_TOP_MIDDLE = 0b00100000;
    public static final int MASK_TILESHAPE_TOP_LEFT = 0b01000000;
    public static final int MASK_TILESHAPE_MIDDLE_LEFT = 0b10000000;
    
    private Client client;
    private HashPool<ClientOutput> coPool;
    private boolean startedSequence;
    
    private int width;
    private int height;
    
    private byte[] tileInfo;
    private byte[] tileShape;
    private short[] tileMaterial;
    
    private Map<Short, Material> palette;

    private PhysicsWorld physicsWorld;
    private Map<Long, Entity> entities;
    private List<Long> entitiesToCreate;
    
    private boolean renderReady;
    private boolean firstRender;
    private WorldRenderer renderer;
    
    private World world;
    
    public ClientsideWorld(Client client)
    {
        this.client = client;
        palette = new HashMap<Short, Material>();
        physicsWorld = new PhysicsWorld();
        entities = new HashMap<Long, Entity>();
        entitiesToCreate = new ArrayList<Long>();
        renderer = new WorldRenderer(this);

        startedSequence = false;
        reset(0, 0);
    }
    
    @Override
    public boolean isServer()
    {
        return false;
    }

    @SuppressWarnings("unchecked")
    public void fetch()
    {
        coPool = (HashPool<ClientOutput>) this.client.getProperty("coPool");
    }
    
    public void reset(int width, int height)
    {
        this.width = width;
        this.height = height;
        
        tileInfo = new byte[width * height];
        tileShape = new byte[width * height];
        tileMaterial = new short[width * height];
        
        palette.clear();

        renderReady = false;
        firstRender = true;
    }
    
    public void requestInitialData()
    {
        startedSequence = false;
    }
    
    public void setRenderReady()
    {
        renderReady = true;
    }
    
    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }
    
    public void addPaletteMember(short key, Material material)
    {
        palette.put(key, material);
    }
    
    public int getPaletteSize()
    {
        return palette.size();
    }
    
    public Material getPaletteMaterial(short key)
    {
        return palette.get(key);
    }
    
    public List<Material> getPaletteValues()
    {
        return new ArrayList<Material>(palette.values());
    }
    
    public Set<Short> getPaletteKeys()
    {
        return new HashSet<Short>(palette.keySet());
    }
    
    @Override
    public byte getTileInfo(int x, int y)
    {
        return tileInfo[y * width + x];
    }

    @Override
    public byte getTileShape(int x, int y)
    {
        return tileShape[y * width + x];
    }

    @Override
    public short getTileMaterial(int x, int y)
    {
        return tileMaterial[y * width + x];
    }
    
    public World getWorld()
    {
        return world;
    }
    
    @Override
    public void setTileInfo(int x, int y, byte info)
    {
        tileInfo[y * width + x] = info;
    }
    
    @Override
    public void setTileShape(int x, int y, byte shape)
    {
        tileShape[y * width + x] = shape;
    }
    
    @Override
    public void setTileMaterial(int x, int y, short material)
    {
        tileMaterial[y * width + x] = material;
    }
    
    public PhysicsWorld getPhysicsWorld()
    {
        return physicsWorld;
    }
    
    @Override
    public void addEntity(Entity entity)
    {
        entities.put(entity.getID(), entity);
    }

    @Override
    public Entity getEntity(long id)
    {
        return entities.get(id);
    }
    
    public Set<Long> getEntityIDSet()
    {
        return new HashSet<Long>(entities.keySet());
    }

    @Override
    public void removeEntity(long id)
    {
        entities.remove(id);
    }
    
    public void createEntity(long id)
    {
        entitiesToCreate.add(id);
    }
    
    public void update(float timeDelta)
    {
        if(!startedSequence && ((NetStage) client.getProperty("networkStage")) == NetStage.USER)
        {
            coPool.get("Lime::InitialWorldRequest").handle();
            startedSequence = true;
        }
        
        for(Long entityID : entitiesToCreate)
            entities.get(entityID).create(physicsWorld);
        physicsWorld.update(timeDelta);
    }
    
    public void render()
    {
        if(renderReady)
        {
            if(firstRender)
            {
                renderer.recompile();
                firstRender = false;
            }
            
            renderer.render();
        }
        
        List<Entity> entityList = new ArrayList<Entity>(entities.values());
        for(Entity entity : entityList)
            entity.render();
    }
}
