package net.lodoma.lime.world.entity;

import org.jbox2d.dynamics.World;

import net.lodoma.lime.util.Vector2;
import net.lodoma.lime.world.TileGrid;

public class TestEntity1 extends Entity
{
    private PolygonSegment mainBody;
    private CircleSegment wheel;
    private RevoluteBodyJoint motor;
    
    public TestEntity1(World world, TileGrid tileGrid)
    {
        super(world, tileGrid);
        
        build();
        assemble();
    }
    
    private void build()
    {
        mainBody = new PolygonSegment(
        new Vector2[]
        {
            new Vector2(0  , 0  ),
            new Vector2(100, 0  ),
            new Vector2(100, 100),
            new Vector2(0  , 100)
        });
        
        wheel = new CircleSegment(50);
        
        motor = new RevoluteBodyJoint(
            mainBody,
            wheel,
            new Vector2(50, 0),
            new Vector2(0, 0)
        );
    }
    
    private void assemble()
    {
        body.addBodySegment(mainBody);
        body.addBodySegment(wheel);
        body.addBodyJoint(motor);
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
