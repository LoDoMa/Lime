package net.lodoma.lime.world.material;

import net.lodoma.lime.util.AnnotationHelper;
import net.lodoma.lime.world.material.property.Rendered;
import net.lodoma.lime.world.material.property.Solid;

@Solid
public abstract class Material
{
    public final boolean solid;
    public final boolean rendered;
    
    public Material()
    {
        solid = AnnotationHelper.isAnnotationPresent(getClass(), Solid.class);
        rendered = AnnotationHelper.isAnnotationPresent(getClass(), Rendered.class);
    }
}
