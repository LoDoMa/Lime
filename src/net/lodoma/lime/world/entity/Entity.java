package net.lodoma.lime.world.entity;

import net.lodoma.lime.world.TileGrid;

public abstract class Entity
{
    protected TileGrid tileGrid;
    protected EntityBody body;
    
    public Entity(TileGrid tileGrid)
    {
        this.tileGrid = tileGrid;
        this.body = new EntityBody();
    }
    
    public abstract void update(float timeDelta);
    public abstract void render();
}
