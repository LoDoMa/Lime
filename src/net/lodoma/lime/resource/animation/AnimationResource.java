package net.lodoma.lime.resource.animation;

import java.io.IOException;

import net.lodoma.lime.Lime;
import net.lodoma.lime.resource.Resource;

public class AnimationResource extends Resource
{
    public Animation animation;
    
    @Override
    public void update(double timeDelta)
    {
        if (animation != null)
            animation.update((float) timeDelta);
    }
    
    @Override
    public void create()
    {
        try
        {
            animation = AnimationLoader.load(name);
            animation.start();
        }
        catch(IOException e)
        {
            Lime.LOGGER.C("Failed to load animation " + name);
            Lime.LOGGER.log(e);
            Lime.forceExit(e);
        }
    }
    
    @Override
    public void destroy()
    {
        animation.delete();
    }
    
    @Override
    public Object get()
    {
        return animation;
    }
}
