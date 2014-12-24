package net.lodoma.lime.world.entity.physics;

public class IntersectionEvent implements Comparable<IntersectionEvent>
{
    public double time;
    public int entityID1;
    public int entityID2;
    
    @Override
    public int compareTo(IntersectionEvent other)
    {
        if (time < other.time)
            return -1;
        if (time > other.time)
            return 1;
        return 0;
    }
}
