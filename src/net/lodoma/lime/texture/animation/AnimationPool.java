package net.lodoma.lime.texture.animation;

import java.util.HashSet;
import java.util.Set;

public class AnimationPool
{
    private static Set<Animation> loadedAnimations = new HashSet<Animation>();
    
    public static void add(Animation animation)
    {
        synchronized (loadedAnimations)
        {
            loadedAnimations.add(animation);
        }
    }
    
    public static void remove(Animation animation)
    {
        synchronized (loadedAnimations)
        {
            loadedAnimations.remove(animation);
        }
    }
    
    public static void updateAll(double timeDelta)
    {
        synchronized (loadedAnimations)
        {
            for (Animation animation : loadedAnimations)
                animation.update((float) timeDelta);
        }
    }
    
    public static void clear()
    {
        synchronized (loadedAnimations)
        {
            for (Animation animation : loadedAnimations)
                animation.delete();
            loadedAnimations.clear();
        }
    }
}
