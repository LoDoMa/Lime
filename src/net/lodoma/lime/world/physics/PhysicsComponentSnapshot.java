package net.lodoma.lime.world.physics;

import java.util.ArrayList;
import java.util.List;

import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.util.Vector2;
import net.lodoma.lime.world.gfx.Vertex;
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
    
    public void destroy()
    {
        for (PhysicsShapeSnapshot shape : shapes)
            shape.destroy();
    }
    
    public void getVertices(List<Vertex> vertices)
    {
        List<Vertex> verts = new ArrayList<Vertex>();
        for (PhysicsShapeSnapshot shape : shapes)
            shape.getVertices(verts);
        for (Vertex v : verts)
        {
            Vector2 rv = new Vector2(v.x, v.y).rotate(angle);
            v.x = rv.x + position.x;
            v.y = rv.y + position.y;
        }
        vertices.addAll(verts);
    }
    
    public void render()
    {
        glPushMatrix();
        glTranslatef(position.x, position.y, 0.0f);
        glRotatef((float) Math.toDegrees(angle), 0.0f, 0.0f, 1.0f);
        
        for (PhysicsShapeSnapshot shape : shapes)
            shape.render();
        
        glPopMatrix();
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
