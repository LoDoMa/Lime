package net.lodoma.lime.util;


public class Transform
{
    private Vector2 position;
    private float angle;
    
    public Transform(Vector2 position, float angle)
    {
        this.position = position;
        this.angle = angle;
    }
    
    public Vector2 getPosition()
    {
        return position;
    }
    
    public float getAngle()
    {
        return angle;
    }
    
    public void setAngle(float angle)
    {
        this.angle = angle;
    }
}
