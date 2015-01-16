package net.lodoma.lime.world.physics;

import net.lodoma.lime.util.Vector2;

public class WorldDefinition
{
    public Vector2 gravity;
    
    public WorldDefinition()
    {
        this.gravity = new Vector2(0.0f, 0.0f);
    }
}
