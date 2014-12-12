package net.lodoma.lime.util;

public class GeometryHelper
{
    public static float ccw(Vector2 a, Vector2 b, Vector2 c)
    {
        return a.x * (b.y - c.y)
             + b.x * (c.y - a.y)
             + c.x * (a.y - b.y);
    }
    
    public static int ccwi(Vector2 a, Vector2 b, Vector2 c)
    {
        float ccw = ccw(a, b, c);
        if (ccw < 0) return -1;
        else if (ccw > 0) return 1;
        return 0;
    }
    
    public static float triangleArea_noabs(Vector2 v1, Vector2 v2, Vector2 v3)
    {
        return ((v2.x - v1.x) * (v3.y - v1.y) - (v3.x - v1.x) * (v2.y - v1.y)) / 2.0f;
    }
    
    public static float triangleArea(Vector2 v1, Vector2 v2, Vector2 v3)
    {
        return Math.abs(triangleArea_noabs(v1, v2, v3));
    }
    
    public static boolean pointInsideQuad(Vector2 q1, Vector2 q2, Vector2 q3, Vector2 q4, Vector2 p)
    {
        float area1 = triangleArea(q1, p, q4);
        float area2 = triangleArea(q4, p, q3);
        float area3 = triangleArea(q3, p, q2);
        float area4 = triangleArea(p, q2, q1);
        float areasum1 = area1 + area2 + area3 + area4;

        float area5 = triangleArea(q1, q2, q3);
        float area6 = triangleArea(q1, q3, q4);
        float areasum2 = area5 + area6;
        
        return areasum1 <= areasum2;
    }
}
