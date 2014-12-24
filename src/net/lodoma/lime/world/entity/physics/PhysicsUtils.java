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
    
    public static void getPostCollisionVelocity(Vector2 pi, Vector2 vi, float mi, Vector2 pj, Vector2 vj, float mj, Vector2 nvi, Vector2 nvj)
    {
        double aij = -Math.atan2(pj.x - pi.x, pj.y - pi.y);
        Vector2 tni = new Vector2((float) (vi.x * Math.cos(aij) + vi.y * Math.sin(aij)), (float) (-vi.x * Math.sin(aij) + vi.y * Math.cos(aij)));
        Vector2 tnj = new Vector2((float) (vj.x * Math.cos(aij) + vj.y * Math.sin(aij)), (float) (-vj.x * Math.sin(aij) + vj.y * Math.cos(aij)));
        
        double pij = mi * tni.y + mj * tnj.y;
        double eij = mi * tni.y * tni.y + mj * tnj.y * tnj.y;
        
        double a = mi * mi + mi * mj;
        double b = -mi * pij;
        double c = pij * pij - mj * eij;

        double ni1 = (-b + Math.sqrt(b * b - a * c)) / a;
        double ni2 = (-b - Math.sqrt(b * b - a * c)) / a;
        
        double ni = ni1;
        if (Math.signum(ni) == Math.signum(tni.y))
            ni = ni2;
        double nj = (pij - mi * ni) / mj;
        
        Vector2 tni2 = new Vector2(tni.x, (float) ni);
        Vector2 tnj2 = new Vector2(tnj.x, (float) nj);

        nvi.set((float) (tni2.x * Math.cos(aij) - tni2.y * Math.sin(aij)), (float) (tni2.x * Math.sin(aij) + tni2.y * Math.cos(aij)));
        nvj.set((float) (tnj2.x * Math.cos(aij) - tnj2.y * Math.sin(aij)), (float) (tnj2.x * Math.sin(aij) + tnj2.y * Math.cos(aij)));
    }
}
