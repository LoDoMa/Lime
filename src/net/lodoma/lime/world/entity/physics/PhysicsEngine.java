package net.lodoma.lime.world.entity.physics;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.function.Consumer;

import net.lodoma.lime.world.World;
import net.lodoma.lime.world.entity.Entity;

public class PhysicsEngine
{
    private class AvoidEvent implements Comparable<AvoidEvent>
    {
        public int time;
        public int entityID1;
        public int entityID2;
        
        @Override
        public int compareTo(AvoidEvent other)
        {
            if (time < other.time) return -1;
            if (time > other.time) return 1;
            return 0;
        }
    }
    
    public PriorityQueue<IntersectionEvent> queue;
    public PriorityQueue<AvoidEvent> avoid;
    
    public World world;
    
    public double oldTime;
    public double time;
    
    public PhysicsEngine(World world)
    {
        this.world = world;
        
        queue = new PriorityQueue<IntersectionEvent>();
        avoid = new PriorityQueue<AvoidEvent>();
    }
    
    public void updateQueue(int entityID)
    {
        Iterator<IntersectionEvent> it = queue.iterator();
        while (it.hasNext())
        {
            IntersectionEvent event = it.next();
            if (event.entityID1 == entityID ||
                event.entityID2 == entityID)
                it.remove();
        }
        
        Entity entity = world.entityPool.get(entityID);
        
        world.entityPool.foreach(new Consumer<Entity>()
        {
            @Override
            public void accept(Entity other)
            {
                if (entity.identifier == other.identifier)
                    return;
                
                IntersectionEvent event = entity.intersects(other);
                if (event != null)
                {
                    event.time += time;
                    queue.add(event);
                }
            }
        });
    }
    
    public void updateAvoid()
    {
        Iterator<AvoidEvent> it = avoid.iterator();
        while (it.hasNext())
        {
            AvoidEvent event = it.next();
            event.time--;
            if (event.time < 0)
                it.remove();
        }
    }
    
    public void update(double timeDelta)
    {
        updateAvoid();
        
        time += timeDelta;
        while (!queue.isEmpty())
        {
            IntersectionEvent nextEvent = queue.peek();
            double nextTime = nextEvent.time;
            
            if (time >= nextTime)
            {
                Entity entity1 = world.entityPool.get(nextEvent.entityID1);
                Entity entity2 = world.entityPool.get(nextEvent.entityID2);

                boolean isAvoided = false;
                Iterator<AvoidEvent> it2 = avoid.iterator();
                while (it2.hasNext())
                {
                    AvoidEvent event = it2.next();
                    if ((event.entityID1 == entity1.identifier && event.entityID2 == entity2.identifier) ||
                        (event.entityID1 == entity2.identifier && event.entityID2 == entity1.identifier))
                    {
                        isAvoided = true;
                        break;
                    }
                }

                queue.remove();
                if (isAvoided) continue;
                   
                time -= nextTime;
                oldTime -= nextTime;
                Iterator<IntersectionEvent> it = queue.iterator();
                while (it.hasNext())
                {
                    IntersectionEvent event = it.next();
                    event.time -= nextTime;
                }
                
                entity1.skipSimulation = true;
                entity2.skipSimulation = true;
                
                AvoidEvent avoidEvent = new AvoidEvent();
                avoidEvent.time = 1;
                avoidEvent.entityID1 = entity1.identifier;
                avoidEvent.entityID2 = entity2.identifier;
                avoid.add(avoidEvent);
                
                float simtime1 = (float) (-oldTime);
                entity1.simulate(simtime1);
                entity2.simulate(simtime1);
                
                entity1.collideWith(entity2);
                
                float simtime2 = (float) time;
                entity1.simulate(simtime2);
                entity2.simulate(simtime2);

                updateQueue(entity1.identifier);
                updateQueue(entity2.identifier);
            }
            else
                break;
        }
        
        oldTime = time;
        
        world.entityPool.foreach(new Consumer<Entity>()
        {
            @Override
            public void accept(Entity entity)
            {
                if (!entity.skipSimulation)
                    entity.simulate((float) timeDelta);
                else
                    entity.skipSimulation = false;
            }
        });
    }
}
