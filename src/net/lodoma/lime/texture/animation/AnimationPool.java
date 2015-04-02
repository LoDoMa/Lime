package net.lodoma.lime.texture.animation;

import java.util.HashSet;
import java.util.Set;

public class AnimationPool
{
    private static Set<Animation> loadedAnimations = new HashSet<Animation>();
    
    public static void add(Animation animation)
    {
        loadedAnimations.add(animation);
    }
    
    public static void remove(Animation animation)
    {
        loadedAnimations.remove(animation);
    }
    
    public static void updateAll(double timeDelta)
    {
        for (Animation animation : loadedAnimations)
            animation.update((float) timeDelta);
    }
    
    public static void clear()
    {
        for (Animation animation : loadedAnimations)
            animation.delete();
        loadedAnimations.clear();
    }
}
