package net.lodoma.lime.gui.exp;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.util.ArrayList;
import java.util.List;

import net.lodoma.lime.gui.Color;
import net.lodoma.lime.util.Vector2;

public class GContainer extends GComponent
{
    private final List<GComponent> components = new ArrayList<GComponent>();
    
    public int getComponentCount()
    {
        return components.size();
    }
    
    public GComponent getComponent(int index)
    {
        return components.get(index);
    }
    
    public GComponent[] getComponents()
    {
        int compc = getComponentCount();
        GComponent[] array = new GComponent[compc];
        components.toArray(array);
        return array;
    }
    
    public void addComponent(GComponent component)
    {
        components.add(component);
    }
    
    public void removeComponent(int index)
    {
        components.remove(index);
    }
    
    public void removeComponent(GComponent component)
    {
        components.remove(component);
    }
    
    public void removeAll()
    {
        components.clear();
    }
    
    @Override
    public void update()
    {
        int compc = getComponentCount();
        for(int i = 0; i < compc; i++)
        {
            GComponent component = getComponent(i);
            component.update();
        }
    }
    
    @Override
    public void render()
    {
        Color color = getColor();
        color.setGL();
        
        glPushMatrix();
        {
            Vector2 translation = getTranslation();
            float rotation = getRotation();
            
            glTranslatef(translation.x, translation.y, 0.0f);
            glRotatef(rotation, 0.0f, 0.0f, 1.0f);
            
            GShape shape = getShape();
            shape.render();
            
            int compc = getComponentCount();
            for(int i = 0; i < compc; i++)
            {
                GComponent component = getComponent(i);
                component.render();
            }
        }
        glPopMatrix();
    }
}
