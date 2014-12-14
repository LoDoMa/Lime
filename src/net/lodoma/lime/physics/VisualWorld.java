package net.lodoma.lime.physics;

import java.util.function.Consumer;

import net.lodoma.lime.util.IdentityPool;

public class VisualWorld
{
    public IdentityPool<Shape> shapePool;
    
    public VisualWorld()
    {
        shapePool = new IdentityPool<Shape>();
    }
    
    public void render()
    {
        // NOTE: The Consumer here should probably be in a final field
        shapePool.foreach(new Consumer<Shape>()
        {
            public void accept(Shape shape)
            {
                shape.render();
            }
        });
    }
}
