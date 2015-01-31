package net.lodoma.lime.gui.exp;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.lodoma.lime.util.Vector2;

public class UIObject
{
    public final Vector2 position = new Vector2(0.0f, 0.0f);
    public final Vector2 dimensions = new Vector2(0.0f, 0.0f);

    /* MEMORY: Each UI object also functions as a container.
       Kind of a waste, but there shouldn't ever be enough UI objects
       for this to be a problem. */
    public final List<UIObject> children = new ArrayList<UIObject>();
    
    public void foreachChild(Consumer<UIObject> consumer)
    {
        for (UIObject child : children)
            consumer.accept(child);
    }
    
    public void update(double timeDelta)
    {
        for (UIObject object : children)
            object.update(timeDelta);
    }
    
    public void render()
    {
        for (UIObject object : children)
            object.render();
    }
}
