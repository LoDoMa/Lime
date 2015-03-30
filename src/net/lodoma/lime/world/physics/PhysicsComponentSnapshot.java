package net.lodoma.lime.world.physics;

import net.lodoma.lime.Lime;
import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.util.Vector2;

import static org.lwjgl.opengl.GL11.*;

public class PhysicsComponentSnapshot implements Identifiable<Integer>
{
    public int identifier;
    
    public Vector2 position;
    public float angle;
    
    public PhysicsComponentType type;
    public PhysicsShapeSnapshot[] shapes;
    
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
        for (PhysicsShapeSnapshot shape : shapes)
            compoDefinition.shapes.add(shape.toShape());
        
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
        glPushMatrix();
        glTranslatef(position.x, position.y, 0.0f);
        glRotatef((float) Math.toDegrees(angle), 0.0f, 0.0f, 1.0f);
        
        for (PhysicsShapeSnapshot shape : shapes)
            shape.debugRender();
        
        glPopMatrix();
    }
}
