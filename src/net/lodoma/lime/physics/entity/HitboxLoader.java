package net.lodoma.lime.physics.entity;

import org.w3c.dom.Element;

public class HitboxLoader
{
    public static Hitbox loadHitbox(Element hitboxElement) throws HitboxLoaderException
    {
        try
        {
            
            return null;
        }
        catch(RuntimeException e)
        {
            throw new HitboxLoaderException(e);
        }
    }
}
