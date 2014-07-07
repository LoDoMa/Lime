package net.lodoma.lime.mask;

import java.util.ArrayList;
import java.util.List;

public class LayeredMask extends Mask
{
    protected RenderingOrder order;
    protected List<Mask> layers;
    
    public LayeredMask(RenderingOrder order)
    {
        this.order = order;
        layers = new ArrayList<Mask>();
    }
    
    public void addLayer(Mask layer)
    {
        layers.add(layer);
    }
    
    @Override
    public void render()
    {
        int size = layers.size();
        
             if(order == RenderingOrder.TOP_TO_BOTTOM)
            for(int i = size - 1; i >= 0; i--)
                layers.get(i).render();
        else if(order == RenderingOrder.BOTTOM_TO_TOP)
            for(int i = 0; i < size; i++)
                layers.get(i).render();
    }
    
    @Override
    public void destroy()
    {
        int size = layers.size();
        for(int i = 0; i < size; i++)
            layers.get(i).destroy();
        
        super.destroy();
    }
}
