package net.lodoma.lime.world;

import net.lodoma.lime.mask.Mask;
import net.lodoma.lime.physics.PhysicsBody;
import net.lodoma.lime.physics.PhysicsBodyType;
import net.lodoma.lime.physics.PhysicsWorld;
import net.lodoma.lime.util.Vector2;

public class Platform
{
    private Mask mask;
    private PhysicsBody body;
    
    public Platform(Mask mask, Vector2 offset, Vector2... vertices)
    {
        this.mask = mask;
        
        body = new PhysicsBody();
        body.setBodyType(PhysicsBodyType.STATIC);
        body.setPolygonShape(vertices);
        body.setPosition(offset);
        
        body.reload();
    }
    
    public Platform(Vector2 offset, Vector2... vertices)
    {
        this(null, offset, vertices);
    }

    public Platform(Vector2... vertices)
    {
        this(null, new Vector2(0.0f, 0.0f), vertices);
    }
    
    public void setMask(Mask mask)
    {
        this.mask = mask;
    }
    
    public void load(PhysicsWorld world)
    {
        body.create(world);
    }
    
    public void unload(PhysicsWorld world)
    {
        body.destroy(world);
    }
    
    public void render()
    {
        if(mask != null)
            mask.render();
    }
}
