package net.lodoma.lime.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import net.lodoma.lime.util.Vector2;

public class UIObject
{
    /* An object can currently only have a parent if it is added
       to another object. An object should never be a child of multiple
       objects. */
    private UIObject parent;
    
    /* If an object's parent has a layout, the object's local position
       and dimensions are set by it. */
    private UILayout layout;
    
    protected final Vector2 localPosition = new Vector2(0.0f, 0.0f);
    protected final Vector2 localDimensions = new Vector2(0.0f, 0.0f);
    
    /* MEMORY: Each UI object also functions as a container.
       Kind of a waste, but there shouldn't ever be enough UI objects
       for this to be a problem. */
    private final List<UIObject> children = new ArrayList<UIObject>();
    
    public final UIObject getParent()
    {
        return parent;
    }
    
    public final boolean hasParent()
    {
        return parent != null;
    }
    
    public final void setLayout(UILayout layout)
    {
        this.layout = layout;
        layout.setObject(this);
        layout.rebuild();
    }

    /* A lot of Vector2 objects are instantiated during execution.
       This helps lower that number down. It doesn't help thread
       safety. UI isn't thread safe. */
    private final Vector2 cacheVector = new Vector2();
    
    /* Note that this always returns exactly the same Vector2 instance.
       If you need to store this somewhere, clone it. */
    public Vector2 getPosition()
    {
        cacheVector.set(getLocalPosition());
        
        if (parent != null)
            cacheVector.addLocal(parent.getPosition());
        
        return cacheVector;
    }

    /* Note that this always returns exactly the same Vector2 instance.
       If you need to store this somewhere, clone it. */
    public Vector2 getDimensions()
    {
        cacheVector.set(getLocalDimensions());
        
        return cacheVector;
    }
    
    /* Returns a reference to the local position. Use this
       to either get or set the position offset. */
    public Vector2 getLocalPosition()
    {
        return localPosition;
    }

    /* Returns a reference to the local dimensions. Use this
       to either get or set the position offset. */
    public Vector2 getLocalDimensions()
    {
        return localDimensions;
    }
    
    public final void addChild(UIObject object)
    {
        /* An object should never be a child of multiple objects.
           If the object already has a parent, complain. */
        if (object.parent != null)
            throw new IllegalStateException();
        
        object.parent = this;
        
        children.add(object);
        
        // Rebuild the layout when a new child is added.
        if (layout != null)
            layout.rebuild();
    }
    
    /* NOTE: we don't check if this object actually contains
             the object we are removing. */
    public final void removeChild(UIObject object)
    {
        children.remove(object);
        
        // The removed object doesn't have a parent anymore.
        object.parent = null;

        // Rebuild the layout when a child is removed.
        if (layout != null)
            layout.rebuild();
    }
    
    public final void foreachChild(BiFunction<UIObject, Integer, Boolean> consumer)
    {
        for (int i = 0; i < children.size(); i++)
            if (consumer.apply(children.get(i), i).booleanValue())
                break;
    }

    /* Update all children. Children should be updated after the parent.
       super.update(timeDelta) should be called last. */
    public void update(double timeDelta)
    {
        for (UIObject object : children)
            object.update(timeDelta);
    }

    /* Render all children. Children should be rendered after the parent.
       super.render() should be called last. */
    public void render()
    {
        for (UIObject object : children)
            object.render();
    }
}
