package net.lodoma.lime.world.material;

import java.util.UUID;

import net.lodoma.lime.util.AnnotationHelper;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.world.material.property.Rendered;
import net.lodoma.lime.world.material.property.Solid;
import net.lodoma.lime.world.material.property.Texture;

public class Material
{
    public final UUID uuid;
    
    public final boolean solid;
    public final boolean rendered;
    public final int texture;
    
    public Material()
    {
        solid = AnnotationHelper.isAnnotationPresent(getClass(), Solid.class);
        rendered = AnnotationHelper.isAnnotationPresent(getClass(), Rendered.class);
        if(rendered && AnnotationHelper.isAnnotationPresent(getClass(), Texture.class))
        {
            String name = ((Texture) AnnotationHelper.getAnnotation(getClass(), Texture.class)).textureName();
            texture = HashHelper.hash32(name);
        }
        else
        {
            texture = 0;
        }
        
        long lsb = 0;
        if(solid) lsb |= (long) 1 << 63;
        if(rendered) lsb |= (long) 1 << 62;
        
        long msb = 0;
        msb |= (long) texture << 32;
        
        uuid = new UUID(msb, lsb);
    }
    
    public Material(UUID uuid)
    {
        this.uuid = uuid;
        
        long lsb = uuid.getLeastSignificantBits();
        solid = ((lsb >> 63) & 0x1) != 0;
        rendered = ((lsb >> 62) & 0x1) != 0;
        
        long msb = uuid.getMostSignificantBits();
        texture = (int) ((msb >> 32) & 0xFFFFFFFF);
    }
}
