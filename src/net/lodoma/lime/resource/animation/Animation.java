package net.lodoma.lime.resource.animation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.lodoma.lime.Lime;
import net.lodoma.lime.world.gfx.Vertex;

public class Animation
{
    private static Object lock = new Object();
    private static Set<Animation> animations = new HashSet<Animation>();
    private static List<Animation> createList = new ArrayList<Animation>();
    private static List<Animation> destroyList = new ArrayList<Animation>();
    
    public static Animation newAnimation(String name)
    {
        synchronized (lock)
        {
            Animation animation = new Animation();
            animation.name = name;
            animations.add(animation);
            createList.add(animation);
            return animation;
        }
    }
    
    public static void destroyAnimation(Animation animation)
    {
        synchronized (lock)
        {
            animations.remove(animation);
            destroyList.add(animation);
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
            
            for (Animation animation : animations)
                animation.update(timeDelta);
        }
    }
    
    public static void forceDeleteAll()
    {
        synchronized (lock)
        {
            destroyList.addAll(animations);
            destroyList.clear();
        }
    }

    private String name;
    
    public Bone root;
    public Map<String, Float> totalDuration = new HashMap<String, Float>();
    
    private Object animationLock = new Object();
    private float time;
    private String animation;

    public void getVertices(List<Vertex> verts)
    {
        synchronized (animationLock)
        {
            if (root == null)
                return;
            
            if (animation == null)
                return;
            root.getVertices(verts);
        }
    }
    
    private void start()
    {
        synchronized (animationLock)
        {
            if (root == null)
                return;
            
            time = 0.0f;
            root.create();
        }
    }
    
    private void update(float timeDelta)
    {
        synchronized (animationLock)
        {
            if (root == null)
                return;
            
            if (animation == null)
                return;
            time = (time + timeDelta) % totalDuration.get(animation);
            root.update(animation, time);
        }
    }
    
    private void delete()
    {
        synchronized (animationLock)
        {
            if (root == null)
                return;
            
            root.destroy();
        }
    }
    
    public void setAnimationSelection(String selection)
    {
        synchronized (animationLock)
        {
            animation = selection;
        }
    }
}
