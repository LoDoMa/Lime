package net.lodoma.lime.physics.entity;

import java.util.HashMap;
import java.util.Map;

import net.lodoma.lime.mask.ColoredMask;
import net.lodoma.lime.mask.LayeredMask;
import net.lodoma.lime.mask.Mask;
import net.lodoma.lime.mask.MaskShape;
import net.lodoma.lime.mask.MaskShapeLoaderException;
import net.lodoma.lime.mask.RenderingOrder;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.util.XMLHelperException;
import static net.lodoma.lime.util.XMLHelper.*;

import org.w3c.dom.Element;

public class ModelLoader
{
    public static Model loadModel(Element modelElement) throws ModelLoaderException
    {
        try
        {
            Map<Integer, Mask> masks = new HashMap<Integer, Mask>();
            
            Element[] maskElements = getChildElementsByName(modelElement, "mask");
            for(Element maskElement : maskElements)
            {
                String name = getChildValue(maskElement, "name");
                Mask mask = loadMask(maskElement);
                masks.put(HashHelper.hash32(name), mask);
            }
            
            Model model = new Model(masks);
            return model;
        }
        catch(XMLHelperException e)
        {
            throw new ModelLoaderException(e);
        }
    }
    
    private static Mask loadMask(Element maskElement) throws ModelLoaderException
    {
        try
        {
            String maskType = getChildValue(maskElement, "type");
            
            if(maskType.equals("layered"))
            {
                Element[] layers = getChildElementsByName(maskElement, "layer");
                
                Mask[] masks = new Mask[256];
                for(int i = 0; i < layers.length; i++)
                {
                    Mask layer = loadMask(layers[i]);
                    int height = getChildIntegerValue(layers[i], "height");
                    
                    if(height < -128) throw new ArrayIndexOutOfBoundsException("minimum layer height is -128");
                    if(height > 127) throw new ArrayIndexOutOfBoundsException("maximum layer height is 127");
                    
                    int index = height + 128;
                    if(masks[index] != null)
                        throw new RuntimeException("duplicate layer height in a layered mask");
                    masks[index] = layer;
                }
                
                LayeredMask mask = new LayeredMask(RenderingOrder.LAST_IN_FRONT);
                for(int i = 0; i < 256; i++)
                    if(masks[i] != null)
                        mask.addLayer(masks[i]);
                return mask;
            }
            else if(maskType.equals("colored"))
            {
                MaskShape shape = MaskShape.newInstance(getUniqueElement(maskElement, "shape"));
                ColoredMask mask = new ColoredMask(shape);
                return mask;
            }
            
            throw new ModelLoaderException("unknown mask type");
        }
        catch(MaskShapeLoaderException | XMLHelperException e)
        {
            throw new ModelLoaderException(e);
        }
    }
}
