package net.lodoma.lime.world;

import net.lodoma.lime.mask.Mask;
import net.lodoma.lime.util.Vector2;

import org.jbox2d.dynamics.World;

public class Platform
{
    private Mask mask;
    private Vector2[] points;
    
    public Platform(Mask mask, Vector2... points)
    {
        assert points.length == 4;
        
        this.mask = mask;
        this.points = points;
    }

    public Platform(Vector2... points)
    {
        this(null, points);
    }
    
    public void setMask(Mask mask)
    {
        this.mask = mask;
    }
    
    public void load(World world)
    {
        
    }
    
    public void unload(World world)
    {
        
    }
    
    public void render()
    {
        if(mask != null)
            mask.render();
    }
}
