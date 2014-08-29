package net.lodoma.lime.physics;

public interface Collider
{
    public IntersectData collide(Collider other);
}
