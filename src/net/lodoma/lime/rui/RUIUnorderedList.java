package net.lodoma.lime.rui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.util.Vector2;
import static org.lwjgl.opengl.GL11.*;

public class RUIUnorderedList extends RUIElement
{
    protected Map<Integer, RUIElement> indexedChildren;
    private Set<RUIElement> nonIndexedChildren;
    private TreeSet<Integer> indicies;
    
    private float verticalSpacing_c;
    
    public RUIUnorderedList(RUIElement parent)
    {
        super(parent);
        
        indexedChildren = new HashMap<Integer, RUIElement>();
        nonIndexedChildren = new HashSet<RUIElement>();
        indicies = new TreeSet<Integer>();
    }
    
    @Override
    protected void loadDefaultValues()
    {
        synchronized (treeLock)
        {
            super.loadDefaultValues();
            
            values.set("default", "vertical-spacing", RUIValue.SIZE_0);
        }
    }
    
    @Override
    public void loadData(RUIParserData data)
    {
        synchronized (treeLock)
        {
            super.loadData(data);
            
            data.copy("vertical-spacing", RUIValueType.SIZE, values);
        }
    }
    
    @Override
    public void addChild(String name, RUIElement child)
    {
        synchronized (treeLock)
        {
            super.addChild(name, child);
            
            int index = child.values.get("default", "index").toInteger();
            if (index > 0)
            {
                if (indicies.contains(index))
                    nonIndexedChildren.add(indexedChildren.get(index));
                else
                    indicies.add(index);
                indexedChildren.put(index, child);
            }
            else
                nonIndexedChildren.add(child);
        }
    }
    
    @Override
    public void removeChild(String name)
    {
        synchronized (treeLock)
        {
            RUIElement child = getChild(name);
            
            int index = child.values.get("default", "index").toInteger();
            if (index > 0)
            {
                indexedChildren.remove(index);
                indicies.remove(index);
            }
            else
                nonIndexedChildren.remove(child);
            
            super.removeChild(name);
        }
        
    }
    
    @Override
    public void update(double timeDelta)
    {
        synchronized (treeLock)
        {
            super.update(timeDelta);
            
            verticalSpacing_c = values.get(state, "vertical-spacing").toSize();
            if (verticalSpacing_c < 0) verticalSpacing_c /= -Window.viewportHeight;
            else verticalSpacing_c *= dimensions_c.y;
        }
    }
    
    @Override
    protected void updateChildren(double timeDelta)
    {
        for (RUIElement child : nonIndexedChildren)
            child.update(timeDelta);

        float offsetY = dimensions_c.y;
        for (int index : indicies)
        {
            RUIElement child = indexedChildren.get(index);
            offsetY -= child.dimensions_c == null ? 0 : child.dimensions_c.y;
            
            Vector2 originalMousePosition = Input.inputData.currentMousePosition.clone();
            Input.inputData.currentMousePosition.subLocalY(offsetY);
            child.update(timeDelta);
            Input.inputData.currentMousePosition.set(originalMousePosition);
            
            offsetY -= verticalSpacing_c;
        }
    }
    
    @Override
    protected void renderChildren()
    {
        for (RUIElement child : nonIndexedChildren)
            child.render();
        
        glTranslatef(0.0f, dimensions_c.y, 0.0f);
        
        for (int index : indicies)
        {
            RUIElement child = indexedChildren.get(index);
            glTranslatef(0.0f, -child.dimensions_c.y, 0.0f);
            child.render();
            glTranslatef(0.0f, -verticalSpacing_c, 0.0f);
        }
    }
}