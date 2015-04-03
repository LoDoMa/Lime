package net.lodoma.lime.resource.animation;

public class SkeletalAnimation implements Animation
{
    public Bone root;
    public float totalDuration;
    
    private Object lock = new Object();
    private float time;
    
    @Override
    public void start()
    {
        time = 0.0f;
    }

    @Override
    public void update(float timeDelta)
    {
        synchronized (lock)
        {
            time = (time + timeDelta) % totalDuration;
            root.update(time);
        }
    }

    @Override
    public void render()
    {
        synchronized (lock)
        {
            root.render();
        }
    }
    
    @Override
    public void delete()
    {
        
    }
}
