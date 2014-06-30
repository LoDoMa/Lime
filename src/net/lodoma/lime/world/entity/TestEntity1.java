package net.lodoma.lime.world.entity;

import net.lodoma.lime.util.Vector2;
import net.lodoma.lime.world.TileGrid;

public class TestEntity1 extends Entity
{
    public TestEntity1(TileGrid tileGrid)
    {
        super(tileGrid);
        
        body.addBodySegment(new PolygonSegment(
            new Vector2[]
            {
                new Vector2(0  , 0  ),
                new Vector2(100, 0  ),
                new Vector2(50 , 50 )
            }));
    }
    
    @Override
    public void update(float timeDelta)
    {
        
    }
    
    @Override
    public void render()
    {
        
    }
}
