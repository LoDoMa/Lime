package net.lodoma.lime.physics;

import net.lodoma.lime.util.Transform;

public interface Collider
{
    public IntersectData collide(Transform thisTransform, Collider other, Transform otherTransform);
}
