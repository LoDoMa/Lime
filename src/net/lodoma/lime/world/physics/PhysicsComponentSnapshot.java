package net.lodoma.lime.world.physics;

import org.lwjgl.opengl.GL11;

import net.lodoma.lime.Lime;
import net.lodoma.lime.texture.Texture;
import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.util.Vector2;

public class PhysicsComponentSnapshot implements Identifiable<Integer>
{
    public int identifier;
    
    public Vector2 position;
    public float angle;
    
    public PhysicsComponentType type;
    public PhysicsComponentShapeType shapeType;
    public float radius;
    public Vector2[] vertices;
    
    @Override
    public Integer getIdentifier()
    {
        return identifier;
    }
    
    @Override
    public void setIdentifier(Integer identifier)
    {
        this.identifier = identifier;
    }
    
    public PhysicsComponentDefinition createDefinition()
    {
        PhysicsComponentDefinition compoDefinition = new PhysicsComponentDefinition();
        compoDefinition.position.set(position);
        compoDefinition.angle = angle;
        compoDefinition.type = type;
        compoDefinition.shape = shapeType.factory.get();
        switch (shapeType)
        {
        case CIRCLE:
            ((PhysicsComponentCircleShape) compoDefinition.shape).radius = radius;
            break;
        case POLYGON:
            ((PhysicsComponentPolygonShape) compoDefinition.shape).vertices = vertices;
            break;
        }
        
        // Note that snapshots don't have information about density, friction and restitution
        compoDefinition.density = 0.0f;
        compoDefinition.friction = 0.0f;
        compoDefinition.restitution = 0.0f;
        
        try
        {
            compoDefinition.validate();
        }
        catch (InvalidPhysicsComponentException e)
        {
            Lime.LOGGER.C("Failed to validate component definition created from component snapshot");
            Lime.LOGGER.log(e);
            Lime.forceExit(e);
        }
        compoDefinition.create();
        
        return compoDefinition;
    }
    
    public void debugRender()
    {
        switch(shapeType)
        {
        case CIRCLE:
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(position.x, position.y, 0.0f);
            GL11.glRotatef((float) Math.toDegrees(angle), 0.0f, 0.0f, 1.0f);
            GL11.glScalef(radius, radius, 1.0f);

            Texture.NO_TEXTURE.bind();

            GL11.glColor3f(0.7f, 0.7f, 0.7f);
            GL11.glBegin(GL11.GL_TRIANGLE_FAN);

            GL11.glVertex2f(0.0f, 0.0f);
            for (int i = 0; i <= 10; i++)
            {
               float angle = (float) Math.toRadians(i * 360.0 / 10.0);
               GL11.glVertex2f((float) Math.cos(angle), (float) Math.sin(angle));
            }
          
            GL11.glEnd();
            
            GL11.glPopMatrix();
            break;
        }
        case POLYGON:
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(position.x, position.y, 0.0f);
            GL11.glRotatef((float) Math.toDegrees(angle), 0.0f, 0.0f, 1.0f);

            Texture.NO_TEXTURE.bind();

            GL11.glColor3f(0.7f, 0.7f, 0.7f);
            GL11.glBegin(GL11.GL_POLYGON);

            for (int i = 0; i < vertices.length; i++)
               GL11.glVertex2f(vertices[i].x, vertices[i].y);
          
            GL11.glEnd();
            
            GL11.glPopMatrix();
            break;
        }
        }
    }
}
