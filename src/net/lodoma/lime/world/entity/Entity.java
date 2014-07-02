package net.lodoma.lime.world.entity;

import org.jbox2d.dynamics.World;

import net.lodoma.lime.world.TileGrid;

public abstract class Entity
{
    protected World world;
    
    protected TileGrid tileGrid;
    protected EntityBody body;
    
    public Entity(World world, TileGrid tileGrid)
    {
        this.world = world;
        
        this.tileGrid = tileGrid;
        this.body = new EntityBody(this);
    }
    
    public final World getWorld()
    {
        return world;
    }
    
    public final void destroy()
    {
        body.destroy();
    }
    
    public abstract void update(float timeDelta);
    public abstract void render();
}
