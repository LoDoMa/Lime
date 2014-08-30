package net.lodoma.lime.physics;

import java.util.ArrayList;
import java.util.List;

import net.lodoma.lime.util.Vector2;

public class PhysicsWorld
{
    private List<PhysicsObject> objects;
    
    public PhysicsWorld()
    {
        objects = new ArrayList<PhysicsObject>();
    }
    
    public void addPhysicsObject(PhysicsObject object)
    {
        objects.add(object);
    }
    
    public void update(double timeDelta)
    {
        int objectCount = objects.size();

        for(int i = 0; i < objectCount; i++)
        {
            PhysicsObject object = objects.get(i);
            object.getTransform().getPosition().addLocal(object.getVelocity().mul((float) timeDelta));
        }
        
        for(int i = 0; i < objectCount; i++)
            for(int j = i + 1; j < objectCount; j++)
            {
                PhysicsObject object1 = objects.get(i);
                PhysicsObject object2 = objects.get(j);
                IntersectData intersectData = object1.collide(object2);
                
                if(intersectData.intersects())
                {
                    Vector2 direction1 = intersectData.getDirection().normalize();
                    Vector2 direction2 = direction1.reflect(object1.getVelocity().normalize());
                    object1.getVelocity().reflectLocal(direction2);
                    object2.getVelocity().reflectLocal(direction1);
                }
            }
    }
}
