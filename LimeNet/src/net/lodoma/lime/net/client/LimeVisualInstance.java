package net.lodoma.lime.net.client;

import net.lodoma.lime.world.World;

public class LimeVisualInstance
{
    private World world;
    private LimeClient client;
    
    public LimeVisualInstance(LimeClient client)
    {
        client.setProperty("world", world);
    }
}
