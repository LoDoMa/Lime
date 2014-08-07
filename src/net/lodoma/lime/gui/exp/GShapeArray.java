package net.lodoma.lime.gui.exp;

public class GShapeArray extends GShape
{
    private GShape[] array;
    
    public GShapeArray(int length)
    {
        array = new GShape[length];
    }
    
    public int getLength()
    {
        return array.length;
    }
    
    public GShape getShape(int index)
    {
        return array[index];
    }
    
    public void setShape(GShape shape, int index)
    {
        array[index] = shape;
    }
    
    @Override
    public boolean isPointInside(float x, float y)
    {
        int length = getLength();
        for(int i = 0; i < length; i++)
        {
            GShape shape = getShape(i);
            if(shape != null)
                if(shape.isPointInside(x, y))
                    return true;
        }
        return false;
    }
    
    @Override
    public void render()
    {
        int length = getLength();
        for(int i = 0; i < length; i++)
        {
            GShape shape = getShape(i);
            if(shape != null)
                shape.render();
        }
    }
}
