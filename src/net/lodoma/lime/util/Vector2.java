package net.lodoma.lime.util;

public class Vector2
{
    public float x;
    public float y;

    public Vector2()
    {
        x = y = 0;
    }

    public Vector2(float v)
    {
        x = y = v;
    }

    public Vector2(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 v)
    {
        x = v.x;
        y = v.y;
    }
    
    public static Vector2 newInstance(float x, float y)
    {
        return new Vector2(x, y);
    }

    public static Vector2 add(Vector2 v1, float v2)
    {
        return new Vector2(v1.x + v2, v1.y + v2);
    }

    public static Vector2 add(Vector2 v1, Vector2 v2)
    {
        return new Vector2(v1.x + v2.x, v1.y + v2.y);
    }

    public Vector2 add(float v)
    {
        return new Vector2(x + v, y + v);
    }

    public Vector2 add(Vector2 v)
    {
        return new Vector2(x + v.x, y + v.y);
    }

    public static Vector2 addX(Vector2 v1, float v2)
    {
        return new Vector2(v1.x + v2, v1.y);
    }

    public static Vector2 addX(Vector2 v1, Vector2 v2)
    {
        return new Vector2(v1.x + v2.x, v1.y);
    }

    public Vector2 addX(float v)
    {
        return new Vector2(x + v, y);
    }

    public Vector2 addX(Vector2 v)
    {
        return new Vector2(x + v.x, y);
    }

    public void addLocalX(float v)
    {
        x += v;
    }

    public void addLocalX(Vector2 v)
    {
        x += v.x;
    }

    public static Vector2 addY(Vector2 v1, float v2)
    {
        return new Vector2(v1.x, v1.y + v2);
    }

    public static Vector2 addY(Vector2 v1, Vector2 v2)
    {
        return new Vector2(v1.x, v1.y + v2.y);
    }

    public Vector2 addY(float v)
    {
        return new Vector2(x, y + v);
    }

    public Vector2 addY(Vector2 v)
    {
        return new Vector2(x, y + v.y);
    }

    public void addLocalY(float v)
    {
        y += v;
    }

    public void addLocalY(Vector2 v)
    {
        y += v.y;
    }

    public void addLocal(float v)
    {
        x += v;
        y += v;
    }

    public void addLocal(Vector2 v)
    {
        x += v.x;
        y += v.y;
    }

    public static Vector2 sub(Vector2 v1, float v2)
    {
        return new Vector2(v1.x - v2, v1.y - v2);
    }

    public static Vector2 sub(Vector2 v1, Vector2 v2)
    {
        return new Vector2(v1.x - v2.x, v1.y - v2.y);
    }

    public Vector2 sub(float v)
    {
        return new Vector2(x - v, y - v);
    }

    public Vector2 sub(Vector2 v)
    {
        return new Vector2(x - v.x, y - v.y);
    }

    public static Vector2 subX(Vector2 v1, float v2)
    {
        return new Vector2(v1.x - v2, v1.y);
    }

    public static Vector2 subX(Vector2 v1, Vector2 v2)
    {
        return new Vector2(v1.x - v2.x, v1.y);
    }

    public Vector2 subX(float v)
    {
        return new Vector2(x - v, y);
    }

    public Vector2 subX(Vector2 v)
    {
        return new Vector2(x - v.x, y);
    }

    public void subLocalX(float v)
    {
        x -= v;
    }

    public void subLocalX(Vector2 v)
    {
        x -= v.x;
    }

    public static Vector2 subY(Vector2 v1, float v2)
    {
        return new Vector2(v1.x, v1.y - v2);
    }

    public static Vector2 subY(Vector2 v1, Vector2 v2)
    {
        return new Vector2(v1.x, v1.y - v2.y);
    }

    public Vector2 subY(float v)
    {
        return new Vector2(x, y - v);
    }

    public Vector2 subY(Vector2 v)
    {
        return new Vector2(x, y - v.y);
    }

    public void subLocalY(float v)
    {
        y -= v;
    }

    public void subLocalY(Vector2 v)
    {
        y -= v.y;
    }

    public void subLocal(float v)
    {
        x -= v;
        y -= v;
    }

    public void subLocal(Vector2 v)
    {
        x -= v.x;
        y -= v.y;
    }

    public static Vector2 mul(Vector2 v1, float v2)
    {
        return new Vector2(v1.x * v2, v1.y * v2);
    }

    public static Vector2 mul(Vector2 v1, Vector2 v2)
    {
        return new Vector2(v1.x * v2.x, v1.y * v2.y);
    }

    public Vector2 mul(float v)
    {
        return new Vector2(x * v, y * v);
    }

    public Vector2 mul(Vector2 v)
    {
        return new Vector2(x * v.x, y * v.y);
    }

    public static Vector2 mulX(Vector2 v1, float v2)
    {
        return new Vector2(v1.x * v2, v1.y);
    }

    public static Vector2 mulX(Vector2 v1, Vector2 v2)
    {
        return new Vector2(v1.x * v2.x, v1.y);
    }

    public Vector2 mulX(float v)
    {
        return new Vector2(x * v, y);
    }

    public Vector2 mulX(Vector2 v)
    {
        return new Vector2(x * v.x, y);
    }

    public void mulLocalX(float v)
    {
        x *= v;
    }

    public void mulLocalX(Vector2 v)
    {
        x *= v.x;
    }

    public static Vector2 mulY(Vector2 v1, float v2)
    {
        return new Vector2(v1.x, v1.y * v2);
    }

    public static Vector2 mulY(Vector2 v1, Vector2 v2)
    {
        return new Vector2(v1.x, v1.y * v2.y);
    }

    public Vector2 mulY(float v)
    {
        return new Vector2(x, y * v);
    }

    public Vector2 mulY(Vector2 v)
    {
        return new Vector2(x, y * v.y);
    }

    public void mulLocalY(float v)
    {
        y *= v;
    }

    public void mulLocalY(Vector2 v)
    {
        y *= v.y;
    }

    public void mulLocal(float v)
    {
        x *= v;
        y *= v;
    }

    public void mulLocal(Vector2 v)
    {
        x *= v.x;
        y *= v.y;
    }

    public static Vector2 div(Vector2 v1, float v2)
    {
        return new Vector2(v1.x / v2, v1.y / v2);
    }

    public static Vector2 div(Vector2 v1, Vector2 v2)
    {
        return new Vector2(v1.x / v2.x, v1.y / v2.y);
    }

    public Vector2 div(float v)
    {
        return new Vector2(x / v, y / v);
    }

    public Vector2 div(Vector2 v)
    {
        return new Vector2(x / v.x, y / v.y);
    }

    public static Vector2 divX(Vector2 v1, float v2)
    {
        return new Vector2(v1.x / v2, v1.y);
    }

    public static Vector2 divX(Vector2 v1, Vector2 v2)
    {
        return new Vector2(v1.x / v2.x, v1.y);
    }

    public Vector2 divX(float v)
    {
        return new Vector2(x / v, y);
    }

    public Vector2 divX(Vector2 v)
    {
        return new Vector2(x / v.x, y);
    }

    public void divLocalX(float v)
    {
        x /= v;
    }

    public void divLocalX(Vector2 v)
    {
        x /= v.x;
    }

    public static Vector2 divY(Vector2 v1, float v2)
    {
        return new Vector2(v1.x, v1.y / v2);
    }

    public static Vector2 divY(Vector2 v1, Vector2 v2)
    {
        return new Vector2(v1.x, v1.y / v2.y);
    }

    public Vector2 divY(float v)
    {
        return new Vector2(x, y / v);
    }

    public Vector2 divY(Vector2 v)
    {
        return new Vector2(x, y / v.y);
    }

    public void divLocalY(float v)
    {
        y /= v;
    }

    public void divLocalY(Vector2 v)
    {
        y /= v.y;
    }

    public void divLocal(float v)
    {
        x /= v;
        y /= v;
    }

    public void divLocal(Vector2 v)
    {
        x /= v.x;
        y /= v.y;
    }

    public static Vector2 abs(Vector2 v)
    {
        return new Vector2(v.x < 0 ? -v.x : v.x, v.y < 0 ? -v.y : v.y);
    }

    public Vector2 abs()
    {
        return new Vector2(x < 0 ? -x : x, y < 0 ? -y : y);
    }

    public void absLocal()
    {
        x = x < 0 ? -x : x;
        y = y < 0 ? -y : y;
    }

    public static Vector2 neg(Vector2 v)
    {
        return new Vector2(-v.x, -v.y);
    }

    public Vector2 neg()
    {
        return new Vector2(-x, -y);
    }

    public void negLocal()
    {
        x = -x;
        y = -y;
    }

    public float dot(Vector2 v)
    {
        return x * v.x + y * v.y;
    }
    
    public static float dot(Vector2 v1, Vector2 v2)
    {
        return v1.x * v2.x + v1.y * v2.y;
    }
    
    public static float magnitude(Vector2 v)
    {
        return (float) Math.sqrt(v.x * v.x + v.y * v.y);
    }

    public static float magnitude(Vector2 v, Vector2 o)
    {
        float dx = v.x - o.x;
        float dy = v.y - o.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public float magnitudeLocal()
    {
        return (float) Math.sqrt(x * x + y * y);
    }

    public float magnitudeLocal(Vector2 o)
    {
        float dx = x - o.x;
        float dy = y - o.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public static Vector2 normalize(Vector2 v)
    {
        float a = magnitude(v);
        return new Vector2(v.x / a, v.y / a);
    }

    public Vector2 normalize()
    {
        float a = magnitudeLocal();
        return new Vector2(x / a, y / a);
    }

    public void normalizeLocal()
    {
        float a = magnitudeLocal();
        x /= a;
        y /= a;
    }

    public static float dist(Vector2 v1, Vector2 v2)
    {
        return v2.sub(v1).magnitudeLocal();
    }

    public float dist(Vector2 v)
    {
        return sub(v).magnitudeLocal();
    }

    public static Vector2 rotate(Vector2 v, float a)
    {
        float cos = (float) Math.cos(a);
        float sin = -(float) Math.sin(a);
        float nx = v.x * cos - v.y * sin;
        float ny = v.x * sin + v.y * cos;
        return new Vector2(nx, ny);
    }

    public static Vector2 rotateDeg(Vector2 v, float a)
    {
        return rotate(v, (float) Math.toRadians(a));
    }

    public Vector2 rotate(float a)
    {
        float cos = (float) Math.cos(a);
        float sin = -(float) Math.sin(a);
        float nx = x * cos - y * sin;
        float ny = x * sin + y * cos;
        return new Vector2(nx, ny);
    }

    public Vector2 rotateDeg(float a)
    {
        return rotate((float) Math.toRadians(a));
    }

    public void rotateLocal(float a)
    {
        float cos = (float) Math.cos(a);
        float sin = -(float) Math.sin(a);
        float nx = x * cos - y * sin;
        float ny = x * sin + y * cos;
        x = nx;
        y = ny;
    }

    public void rotateLocalDeg(float a)
    {
        rotateLocal((float) Math.toRadians(a));
    }

    public static Vector2 rotateAround(Vector2 c, Vector2 v, float a)
    {
        Vector2 v2 = v.clone();
        v2.subLocal(c);
        v2.rotateLocal(a);
        v2.addLocal(c);
        return v2;
    }

    public static Vector2 rotateAroundDeg(Vector2 c, Vector2 v, float a)
    {
        return rotateAround(c, v, (float) Math.toRadians(a));
    }

    public Vector2 rotateAround(Vector2 c, float a)
    {
        Vector2 v = clone();
        v.subLocal(c);
        v.rotateLocal(a);
        v.addLocal(c);
        return v;
    }

    public Vector2 rotateAroundDeg(Vector2 c, float a)
    {
        return rotateAround(c, (float) Math.toRadians(a));
    }

    public void rotateAroundLocal(Vector2 c, float a)
    {
        subLocal(c);
        rotateLocal(a);
        addLocal(c);
    }

    public void rotateAroundLocalDeg(Vector2 c, float a)
    {
        rotateAroundLocal(c, (float) Math.toRadians(a));
    }

    public static float angle(Vector2 v)
    {
        return (float) Math.atan2(v.x, v.y);
    }

    public static float angleDeg(Vector2 v)
    {
        return (float) Math.toDegrees(Math.atan2(v.x, v.y));
    }

    public float angle()
    {
        return (float) Math.atan2(x, y);
    }

    public float angleDeg()
    {
        return (float) Math.toDegrees(Math.atan2(x, y));
    }

    public static Vector2 floor(Vector2 v)
    {
        return new Vector2((float) Math.floor(v.x), (float) Math.floor(v.y));
    }

    public Vector2 floor()
    {
        return new Vector2((float) Math.floor(x), (float) Math.floor(y));
    }

    public void floorLocal()
    {
        x = (float) Math.floor(x);
        y = (float) Math.floor(y);
    }

    public static Vector2 ceil(Vector2 v)
    {
        return new Vector2((float) Math.ceil(v.x), (float) Math.ceil(v.y));
    }

    public Vector2 ceil()
    {
        return new Vector2((float) Math.ceil(x), (float) Math.ceil(y));
    }

    public void ceilLocal()
    {
        x = (float) Math.ceil(x);
        y = (float) Math.ceil(y);
    }

    public static Vector2 round(Vector2 v)
    {
        return new Vector2(Math.round(v.x), Math.round(v.y));
    }

    public Vector2 round()
    {
        return new Vector2(Math.round(x), Math.round(y));
    }

    public void roundLocal()
    {
        x = Math.round(x);
        y = Math.round(y);
    }

    public Vector2 contain(float rangeMin, float rangeMax)
    {
        return new Vector2(x < rangeMin ? rangeMin : (x > rangeMax ? rangeMax : x),
                           y < rangeMin ? rangeMin : (y > rangeMax ? rangeMax : y));
    }

    public Vector2 contain(float rangeMinX, float rangeMaxX, float rangeMinY, float rangeMaxY)
    {
        return new Vector2(x < rangeMinX ? rangeMinX : (x > rangeMaxX ? rangeMaxX : x),
                           y < rangeMinY ? rangeMinY : (y > rangeMaxY ? rangeMaxY : y));
    }

    public void containLocal(float rangeMin, float rangeMax)
    {
        x = x < rangeMin ? rangeMin : (x > rangeMax ? rangeMax : x);
        y = y < rangeMin ? rangeMin : (y > rangeMax ? rangeMax : y);
    }

    public void containLocal(float rangeMinX, float rangeMaxX, float rangeMinY, float rangeMaxY)
    {
        x = x < rangeMinX ? rangeMinX : (x > rangeMaxX ? rangeMaxX : x);
        y = y < rangeMinY ? rangeMinY : (y > rangeMaxY ? rangeMaxY : y);
    }

    public float containedX(float rangeMin, float rangeMax)
    {
        return x < rangeMin ? rangeMin : (x > rangeMax ? rangeMax : x);
    }

    public Vector2 containX(float rangeMin, float rangeMax)
    {
        return new Vector2(x < rangeMin ? rangeMin : (x > rangeMax ? rangeMax : x), y);
    }

    public void containXLocal(float rangeMin, float rangeMax)
    {
        x = x < rangeMin ? rangeMin : (x > rangeMax ? rangeMax : x);
    }

    public float containedY(float rangeMin, float rangeMax)
    {
        return y < rangeMin ? rangeMin : (y > rangeMax ? rangeMax : y);
    }

    public Vector2 containY(float rangeMin, float rangeMax)
    {
        return new Vector2(x, y < rangeMin ? rangeMin : (y > rangeMax ? rangeMax : y));
    }

    public void containYLocal(float rangeMin, float rangeMax)
    {
        y = y < rangeMin ? rangeMin : (y > rangeMax ? rangeMax : y);
    }

    public static Vector2 contain(Vector2 v, float rangeMin, float rangeMax)
    {
        return new Vector2(v.x < rangeMin ? rangeMin : (v.x > rangeMax ? rangeMax : v.x),
                           v.y < rangeMin ? rangeMin : (v.y > rangeMax ? rangeMax : v.y));
    }

    public static Vector2 contain(Vector2 v, float rangeMinX, float rangeMaxX, float rangeMinY, float rangeMaxY)
    {
        return new Vector2(v.x < rangeMinX ? rangeMinX : (v.x > rangeMaxX ? rangeMaxX : v.x),
                           v.y < rangeMinY ? rangeMinY : (v.y > rangeMaxY ? rangeMaxY : v.y));
    }

    public static void containLocal(Vector2 v, float rangeMin, float rangeMax)
    {
        v.x = v.x < rangeMin ? rangeMin : (v.x > rangeMax ? rangeMax : v.x);
        v.y = v.y < rangeMin ? rangeMin : (v.y > rangeMax ? rangeMax : v.y);
    }

    public static void containLocal(Vector2 v, float rangeMinX, float rangeMaxX, float rangeMinY, float rangeMaxY)
    {
        v.x = v.x < rangeMinX ? rangeMinX : (v.x > rangeMaxX ? rangeMaxX : v.x);
        v.y = v.y < rangeMinY ? rangeMinY : (v.y > rangeMaxY ? rangeMaxY : v.y);
    }

    public static float containedX(Vector2 v, float rangeMin, float rangeMax)
    {
        return v.x < rangeMin ? rangeMin : (v.x > rangeMax ? rangeMax : v.x);
    }

    public static Vector2 containX(Vector2 v, float rangeMin, float rangeMax)
    {
        return new Vector2(v.x < rangeMin ? rangeMin : (v.x > rangeMax ? rangeMax : v.x), v.y);
    }

    public static void containXLocal(Vector2 v, float rangeMin, float rangeMax)
    {
        v.x = v.x < rangeMin ? rangeMin : (v.x > rangeMax ? rangeMax : v.x);
    }

    public static float containedY(Vector2 v, float rangeMin, float rangeMax)
    {
        return v.y < rangeMin ? rangeMin : (v.y > rangeMax ? rangeMax : v.y);
    }

    public static Vector2 containY(Vector2 v, float rangeMin, float rangeMax)
    {
        return new Vector2(v.x, v.y < rangeMin ? rangeMin : (v.y > rangeMax ? rangeMax : v.y));
    }

    public static void containYLocal(Vector2 v, float rangeMin, float rangeMax)
    {
        v.y = v.y < rangeMin ? rangeMin : (v.y > rangeMax ? rangeMax : v.y);
    }
    
    public Vector2 min(Vector2 v)
    {
        return new Vector2(Math.min(x, v.x), Math.min(y, v.y));
    }
    
    public void minLocal(Vector2 v)
    {
        x = Math.min(x, v.x);
        y = Math.min(y, v.y);
    }
    
    public static Vector2 min(Vector2 v1, Vector2 v2)
    {
        return new Vector2(Math.min(v1.x, v2.x), Math.min(v1.y, v2.y));
    }
    
    public Vector2 max(Vector2 v)
    {
        return new Vector2(Math.max(x, v.x), Math.max(y, v.y));
    }
    
    public void maxLocal(Vector2 v)
    {
        x = Math.max(x, v.x);
        y = Math.max(y, v.y);
    }
    
    public static Vector2 max(Vector2 v1, Vector2 v2)
    {
        return new Vector2(Math.max(v1.x, v2.x), Math.max(v1.y, v2.y));
    }
    
    public float minComponent()
    {
        return Math.min(x, y);
    }
    
    public static float minComponent(Vector2 v)
    {
        return Math.min(v.x, v.y);
    }
    
    public float maxComponent()
    {
        return Math.max(x, y);
    }
    
    public static float maxComponent(Vector2 v)
    {
        return Math.max(v.x, v.y);
    }
    
    public Vector2 reflect(Vector2 normal)
    {
        float dot = dot(normal);
        return new Vector2(x - (2.0f * dot * normal.x), y - (2.0f * dot * normal.y));
    }
    
    public void reflectLocal(Vector2 normal)
    {
        float dot = dot(normal);
        x -= 2.0f * dot * normal.x;
        y -= 2.0f * dot * normal.y;
    }
    
    public static Vector2 reflect(Vector2 vector, Vector2 normal)
    {
        float dot = dot(vector, normal);
        return new Vector2(vector.x - (2.0f * dot * normal.x), vector.y - (2.0f * dot * normal.y));
    }

    public static float getX(Vector2 v)
    {
        return v.x;
    }

    public float getX()
    {
        return x;
    }

    public static float getY(Vector2 v)
    {
        return v.y;
    }

    public float getY()
    {
        return y;
    }
    
    public void setX(float x)
    {
        this.x = x;
    }
    
    public static void setX(Vector2 v, float x)
    {
        v.x = x;
    }
    
    public void setY(float y)
    {
        this.y = y;
    }
    
    public static void setY(Vector2 v, float y)
    {
        v.y = y;
    }
    
    public void setX(Vector2 v)
    {
        this.x = v.x;
    }
    
    public static void setX(Vector2 v1, Vector2 v2)
    {
        v1.x = v2.x;
    }
    
    public void setY(Vector2 v)
    {
        this.y = v.y;
    }
    
    public static void setY(Vector2 v1, Vector2 v2)
    {
        v1.y = v2.y;
    }
    
    public void set(Vector2 v)
    {
        x = v.x;
        y = v.y;
    }
    
    public void set(float x, float y)
    {
        this.x = x;
        this.y = y;
    }
    
    public static void set(Vector2 v1, Vector2 v2)
    {
        v1.x = v2.x;
        v1.y = v2.y;
    }
    
    public static void set(Vector2 v, float x, float y)
    {
        v.x = x;
        v.y = y;
    }
    
    public boolean equals(Vector2 v)
    {
        return x == v.x && y == v.y;
    }
    
    public static boolean equals(Vector2 v1, Vector2 v2)
    {
        return v1.x == v2.x && v1.y == v2.y;
    }

    @Override
    public Vector2 clone()
    {
        return new Vector2(x, y);
    }

    @Override
    public String toString()
    {
        return "(" + x + " " + y + ")";
    }
}
