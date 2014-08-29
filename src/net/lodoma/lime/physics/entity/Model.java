package net.lodoma.lime.physics.entity;

import net.lodoma.lime.mask.Mask;

public class Model
{
    private Mask mask;
    
    public Model(Mask mask)
    {
        this.mask = mask;
    }
    
    public Mask getMask()
    {
        return mask;
    }
}
