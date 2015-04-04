package net.lodoma.lime.resource.animation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.lodoma.lime.Lime;

public class Animation
{
    private static Object lock = new Object();
    private static List<Animation> createList = new ArrayList<Animation>();
    private static List<Animation> destroyList = new ArrayList<Animation>();
    private static Map<String, Animation> loadedAnimations = new HashMap<String, Animation>();
    
    public static void referenceUp(String name)
    {
        synchronized (lock)
        {
            Animation animation = loadedAnimations.get(name);
            if (animation == null)
            {
                animation = new Animation();
                animation.name = name;
                loadedAnimations.put(name, animation);
                createList.add(animation);
            }
            animation.refc++;
        }
    }
    
    public static void referenceDown(String name)
    {
        synchronized (lock)
        {
            Animation animation = loadedAnimations.get(name);
            if (animation == null)
                throw new NullPointerException();
            animation.refc--;
            if (animation.refc == 0)
            {
                loadedAnimations.remove(animation.name);
                destroyList.add(animation);
            }
        }
    }
    
    public static void updateAll(float timeDelta)
    {
        synchronized (lock)
        {
            for (Animation animation : createList)
                try
                {
                    AnimationLoader.load(animation, animation.name);
                    animation.start();
                    Lime.LOGGER.D("Loaded animation " + animation.name);
                }
                catch(IOException e)
                {
                    Lime.LOGGER.C("Failed to load animation " + animation.name);
                    Lime.LOGGER.log(e);
                    Lime.forceExit(e);
                }
            
            for (Animation animation : destroyList)
            {
                animation.delete();
                Lime.LOGGER.D("Deleted animation " + animation.name);
            }
            
            createList.clear();
            destroyList.clear();
            
            Collection<Animation> animations = loadedAnimations.values();
            Iterator<Animation> it = animations.iterator();
            while (it.hasNext())
            {
                Animation animation = it.next();
                animation.update(timeDelta);
            }
        }
    }
    
    public static void forceDeleteAll()
    {
        synchronized (lock)
        {
            destroyList.addAll(loadedAnimations.values());
            destroyList.clear();
        }
    }
    
    public static Animation get(String name)
    {
        synchronized (lock)
        {
            Animation animation = loadedAnimations.get(name);
            if (animation == null)
                throw new NullPointerException();
            return animation;
        }
    }

    private String name;
    private int refc = 0;
    
    public Bone root;
    public Map<String, Float> totalDuration = new HashMap<String, Float>();
    public String animation;
    
    private Object animationLock = new Object();
    private float time;

    public void render()
    {
        synchronized (animationLock)
        {
            if (animation == null)
                return;
            root.render();
        }
    }
    
    private void start()
    {
        time = 0.0f;
        root.create();
    }
    
    private void update(float timeDelta)
    {
        synchronized (animationLock)
        {
            if (animation == null)
                return;
            time = (time + timeDelta) % totalDuration.get(animation);
            root.update(animation, time);
        }
    }
    
    private void delete()
    {
        root.destroy();
    }
}
