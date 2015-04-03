package net.lodoma.lime.resource;

import java.util.function.Supplier;

import net.lodoma.lime.resource.animation.AnimationResource;
import net.lodoma.lime.resource.texture.TextureResource;

public enum ResourceType
{
    TEXTURE(() -> new TextureResource()),
    ANIMATION(() -> new AnimationResource());
    
    public final Supplier<Resource> create;
    
    private ResourceType(Supplier<Resource> create)
    {
        this.create = create;
    }
}
