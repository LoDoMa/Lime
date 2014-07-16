package net.lodoma.lime.world.material;

import java.util.UUID;

import net.lodoma.lime.util.AnnotationHelper;
import net.lodoma.lime.world.material.property.Rendered;
import net.lodoma.lime.world.material.property.Solid;

public class Material
{
    public final UUID uuid;
    
    public final boolean solid;
    public final boolean rendered;
    
    public Material()
    {
        solid = AnnotationHelper.isAnnotationPresent(getClass(), Solid.class);
        rendered = AnnotationHelper.isAnnotationPresent(getClass(), Rendered.class);
        
        long lsb = 0;
        if(solid) lsb |= (long) 1 << 63;
        if(rendered) lsb |= (long) 1 << 62;
        
        long msb = 0;
        
        uuid = new UUID(msb, lsb);
    }
    
    public Material(UUID uuid)
    {
        this.uuid = uuid;
        
        long lsb = uuid.getLeastSignificantBits();
        solid = ((lsb >> 63) & 0x1) != 0;
        rendered = ((lsb >> 62) & 0x1) != 0;
        
        @SuppressWarnings("unused")
        long msb = uuid.getMostSignificantBits();
    }
}
