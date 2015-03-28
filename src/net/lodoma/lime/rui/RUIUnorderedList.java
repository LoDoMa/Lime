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

    private int horalign_c;
    private int veralign_c;
    private float verticalSpacing_c;
    
    private float totalSize_c;
    private float offsetY_c;
    
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

            values.set("default", "horizontal-alignment", new RUIValue("left"));
            values.set("default", "vertical-alignment", new RUIValue("top"));
            values.set("default", "vertical-spacing", RUIValue.SIZE_0);
        }
    }
    
    @Override
    public void loadData(RUIParserData data)
    {
        synchronized (treeLock)
        {
            super.loadData(data);

            data.copy("horizontal-alignment", RUIValueType.STRING, values);
            data.copy("vertical-alignment", RUIValueType.STRING, values);
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
            
            String horalign_t = values.get(state, "horizontal-alignment").toString();
            switch (horalign_t)
            {
            case "left": horalign_c = RUIAlignment.ALIGN_LEFT; break;
            case "center": horalign_c = RUIAlignment.ALIGN_CENTER; break;
            case "right": horalign_c = RUIAlignment.ALIGN_RIGHT; break;
            default: throw new IllegalStateException();
            }

            String veralign_t = values.get(state, "vertical-alignment").toString();
            switch (veralign_t)
            {
            case "top": veralign_c = RUIAlignment.ALIGN_TOP; break;
            case "center": veralign_c = RUIAlignment.ALIGN_CENTER; break;
            case "bottom": veralign_c = RUIAlignment.ALIGN_BOTTOM; break;
            default: throw new IllegalStateException();
            }
        }
    }
    
    @Override
    protected void updateChildren(double timeDelta)
    {
        for (RUIElement child : nonIndexedChildren)
            child.update(timeDelta);

        totalSize_c = -verticalSpacing_c;
        for (int index : indicies)
        {
            RUIElement child = indexedChildren.get(index);
            totalSize_c += (child.dimensions_c == null ? 0 : child.dimensions_c.y) + verticalSpacing_c;
        }
        
        if (veralign_c == RUIAlignment.ALIGN_TOP)
            offsetY_c = dimensions_c.y;
        else if (veralign_c == RUIAlignment.ALIGN_CENTER)
            offsetY_c = dimensions_c.y - (dimensions_c.y - totalSize_c) / 2.0f;
        else
            offsetY_c = totalSize_c;
        
        float offsetY = offsetY_c;
        
        for (int index : indicies)
        {
            RUIElement child = indexedChildren.get(index);
            offsetY -= child.dimensions_c == null ? 0 : child.dimensions_c.y;
            
            float cdx = child.dimensions_c == null ? 0 : child.dimensions_c.x;
            float hordiff;
            if (horalign_c == RUIAlignment.ALIGN_LEFT)
                hordiff = 0.0f;
            else if (horalign_c == RUIAlignment.ALIGN_CENTER)
                hordiff = (dimensions_c.x - cdx) / 2.0f;
            else
                hordiff = (dimensions_c.x - cdx);
            
            Vector2 originalMousePosition = Input.inputData.currentMousePosition.clone();
            Input.inputData.currentMousePosition.subLocalX(hordiff);
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
        
        glTranslatef(0.0f, offsetY_c, 0.0f);
        
        for (int index : indicies)
        {
            RUIElement child = indexedChildren.get(index);
            
            float hordiff;
            if (horalign_c == RUIAlignment.ALIGN_LEFT)
                hordiff = 0.0f;
            else if (horalign_c == RUIAlignment.ALIGN_CENTER)
                hordiff = (dimensions_c.x - child.dimensions_c.x) / 2.0f;
            else
                hordiff = (dimensions_c.x - child.dimensions_c.x);

            glTranslatef(0.0f, -child.dimensions_c.y, 0.0f);
            
            glPushMatrix();
            glTranslatef(hordiff, 0.0f, 0.0f);
            child.render();
            glPopMatrix();
            
            glTranslatef(0.0f, -verticalSpacing_c, 0.0f);
        }
    }
}