package net.lodoma.lime.world.entity.physics;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.function.Consumer;

import net.lodoma.lime.world.World;
import net.lodoma.lime.world.entity.Entity;

public class PhysicsEngine
{
    public PriorityQueue<IntersectionEvent> queue;
    
    public World world;
    
    public double oldTime;
    public double time;
    
    public PhysicsEngine(World world)
    {
        this.world = world;
        
        queue = new PriorityQueue<IntersectionEvent>();
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
    
    public void update(double timeDelta)
    {
        time += timeDelta;
        while (!queue.isEmpty())
        {
            IntersectionEvent nextEvent = queue.peek();
            double nextTime = nextEvent.time;
            System.out.println(nextTime);
            
            if (time >= nextTime)
            {
                queue.remove();   
                time -= nextTime;
                oldTime -= nextTime;
                Iterator<IntersectionEvent> it = queue.iterator();
                while (it.hasNext())
                {
                    IntersectionEvent event = it.next();
                    event.time -= nextTime;
                }

                Entity entity1 = world.entityPool.get(nextEvent.entityID1);
                Entity entity2 = world.entityPool.get(nextEvent.entityID2);
                
                entity1.skipSimulation = true;
                entity2.skipSimulation = true;
                
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
