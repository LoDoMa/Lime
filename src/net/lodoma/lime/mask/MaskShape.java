package net.lodoma.lime.mask;

import org.w3c.dom.Element;

import net.lodoma.lime.gui.Color;
import net.lodoma.lime.util.Vector2;
import static net.lodoma.lime.util.XMLHelper.*;

public class MaskShape
{
    public static enum ShapeType
    {
        POLYGON
    }
    
    public static MaskShape newInstance(Element shapeElement) throws MaskShapeLoaderException
    {
        try
        {
            MaskShape shape = new MaskShape();
            
            String type = getChildValue(shapeElement, "type");
            if(type.equals("rectangle"))
            {
                shape.type = ShapeType.POLYGON;
                shape.vertices = new Vector2[4];
                shape.colors = new Color[4];
    
                Vector2 offset = new Vector2(getChildIntegerValue(shapeElement, "offsetx"),
                                             getChildIntegerValue(shapeElement, "offsety"));
                Vector2 dimensions = new Vector2(getChildIntegerValue(shapeElement, "width"),
                                                 getChildIntegerValue(shapeElement, "height"));
    
                shape.vertices[0] = offset.clone();
                shape.vertices[1] = offset.addX(dimensions);
                shape.vertices[2] = offset.add(dimensions);
                shape.vertices[3] = offset.addY(dimensions);
                
                for(int i = 0; i < 4; i++)
                    shape.colors[i] = new Color(1.0f, 0.0f, 0.0f, 1.0f);
            }
            
            return shape;
        }
        catch(RuntimeException e)
        {
            throw new MaskShapeLoaderException(e);
        }
    }
    
    private ShapeType type;
    
    private float radius;
    private Vector2[] vertices;
    private Color[] colors;
    
    public ShapeType getType()
    {
        return type;
    }
    
    public float getRadius()
    {
        return radius;
    }
    
    public Vector2[] getVertices()
    {
        return vertices;
    }
    
    public int getVertexCount()
    {
        return vertices.length;
    }
    
    public Vector2 getVertex(int index)
    {
        return vertices[index];
    }
    
    public Color[] getColors()
    {
        return colors;
    }
    
    public Color getColor(int index)
    {
        return colors[index];
    }
}
