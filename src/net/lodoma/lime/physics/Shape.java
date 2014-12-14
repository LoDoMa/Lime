package net.lodoma.lime.physics;

import java.util.function.Consumer;

import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.util.IdentityPool;

public class Shape implements Identifiable<Integer>
{
    public int identifier;
    public VisualWorld world;
    public IdentityPool<ShapeComponent> componentPool;
    
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
    
    public void render()
    {
        // NOTE: The Consumer here should probably be in a final field
        componentPool.foreach(new Consumer<ShapeComponent>()
        {
            public void accept(ShapeComponent shapeComponent)
            {
                shapeComponent.render();
            }
        });
    }
}
