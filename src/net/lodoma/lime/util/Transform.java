package net.lodoma.lime.util;

import org.lwjgl.opengl.GL11;


public class Transform
{
    private Vector2 position;
    private float angle;
    
    public Transform(Transform copy)
    {
        this.position = new Vector2(copy.position);
        this.angle = copy.angle;
    }
    
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
    
    public void openGLTransform()
    {
        GL11.glTranslatef(position.x, position.y, 0.0f);
        GL11.glRotatef(angle, 0.0f, 0.0f, 1.0f);
    }
}
