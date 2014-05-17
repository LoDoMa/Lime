package net.joritan.jlime.util;

public class Physics
{
    public static Vector2 lineIntersection(float x1, float y1, float x2, float y2,
                                           float x3, float y3, float x4, float y4)
    {
        float denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
        if(denom == 0.0f)
            return null;
        float ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denom;
        float ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / denom;
        if(ua >= 0.0f && ua <= 1.0f && ub >= 0.0f && ub <= 1.0f)
            return new Vector2(x1 + ua * (x2 - x1), y1 + ua * (y2 - y1));
        return null;
    }

    public static boolean linesIntersect(float x1, float y1, float x2, float y2,
                                         float x3, float y3, float x4, float y4)
    {
        float denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
        if(denom == 0.0f)
            return false;
        float ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denom;
        float ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / denom;
        if(ua >= 0.0f && ua <= 1.0f && ub >= 0.0f && ub <= 1.0f)
            return true;
        return false;
    }
}
