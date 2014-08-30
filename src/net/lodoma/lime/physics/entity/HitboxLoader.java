package net.lodoma.lime.physics.entity;

import static net.lodoma.lime.util.XMLHelper.*;

import java.util.HashMap;
import java.util.Map;

import net.lodoma.lime.physics.AABB;
import net.lodoma.lime.physics.Collider;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.util.Vector2;
import net.lodoma.lime.util.XMLHelperException;

import org.w3c.dom.Element;

public class HitboxLoader
{
    public static Hitbox loadHitbox(Element hitboxElement) throws HitboxLoaderException
    {
        try
        {
            Map<Integer, Collider> colliders = new HashMap<Integer, Collider>();
            
            Element[] colliderElements = getChildElementsByName(hitboxElement, "collider");
            for(Element colliderElement : colliderElements)
            {
                String name = getChildValue(colliderElement, "name");
                Collider collider = loadCollider(colliderElement);
                colliders.put(HashHelper.hash32(name), collider);
            }
            
            Hitbox hitbox = new Hitbox(colliders);
            return hitbox;
        }
        catch(XMLHelperException e)
        {
            throw new HitboxLoaderException(e);
        }
    }
    
    private static Collider loadCollider(Element colliderElement) throws HitboxLoaderException
    {
        try
        {
            String type = getChildValue(colliderElement, "type");
            
            Collider collider = null;
            
            if(type.equals("AABB"))
            {
                float width = getChildFloatValue(colliderElement, "width");
                float height = getChildFloatValue(colliderElement, "height");
                collider = new AABB(new Vector2(0.0f), new Vector2(width, height));
            }
            
            if(collider == null)
                throw new HitboxLoaderException("unknown collider type");
            return collider;
        }
        catch(XMLHelperException e)
        {
            throw new HitboxLoaderException(e);
        }
    }
}
