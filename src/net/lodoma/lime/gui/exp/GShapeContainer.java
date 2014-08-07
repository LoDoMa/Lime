package net.lodoma.lime.gui.exp;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class GShapeContainer extends GShape
{
    private final List<GShape> shapes = new ArrayList<GShape>();
    
    public int getShapeCount()
    {
        return shapes.size();
    }
    
    public GShape getShape(int index)
    {
        return shapes.get(index);
    }
    
    public GShape[] getShapes()
    {
        int shapec = getShapeCount();
        GShape[] array = new GShape[shapec];
        shapes.toArray(array);
        return array;
    }
    
    public void addShape(GShape component)
    {
        shapes.add(component);
    }
    
    public void removeShape(int index)
    {
        shapes.remove(index);
    }
    
    public void removeShape(GShape component)
    {
        shapes.remove(component);
    }
    
    public void removeAll()
    {
        shapes.clear();
    }
    
    @Override
    public boolean isPointInside(float x, float y)
    {
        int shapec = getShapeCount();
        for(int i = 0; i < shapec; i++)
        {
            GShape shape = getShape(i);
            if(shape.isPointInside(x, y))
                return true;
        }
        return false;
    }
    
    @Override
    public void render()
    {
        color.setGL();
        
        glPushMatrix();
        {
            glTranslatef(translation.x, translation.y, 0.0f);
            glRotatef(rotation, 0.0f, 0.0f, 1.0f);
            
            int shapec = getShapeCount();
            for(int i = 0; i < shapec; i++)
            {
                GShape shape = getShape(i);
                shape.render();
            }
        }
        glPopMatrix();
    }
}
