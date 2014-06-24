package net.lodoma.lime.world.entity;

import net.lodoma.lime.world.TileGrid;

public abstract class Entity
{
    protected TileGrid tileGrid;
    
    public Entity(TileGrid tileGrid)
    {
        this.tileGrid = tileGrid;
    }
    
    public abstract void update(float timeDelta);
    public abstract void render();
}
