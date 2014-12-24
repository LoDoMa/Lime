package net.lodoma.lime.world.entity.physics;

import net.lodoma.lime.util.Vector2;

public class PhysicsUtils
{
    public static double getIntersectionTime(Vector2 pi, Vector2 vi, float ri, Vector2 pj, Vector2 vj, float rj)
    {
        double dij = ri + rj;
        double dxij = pi.x - pj.x;
        double dyij = pi.y - pj.y;
        double duij = vi.x - vj.x;
        double dvij = vi.y - vj.y;
        
        double a = duij * duij + dvij * dvij;
        double b = dxij * duij + dyij * dvij;
        double c = dxij * dxij + dyij * dyij - dij * dij;
        
        double sc = b * b - a * c;
        if (sc < 0.0) return -1.0;
        
        double s = Math.sqrt(sc);
        double t1 = (-b + s) / a;
        double t2 = (-b - s) / a;
        
        if (t1 < 0.0) return t2;
        if (t2 < 0.0) return t1;
        return Math.min(t1, t2);
    }
}
