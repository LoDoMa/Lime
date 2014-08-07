package net.lodoma.lime.gui.exp;

public class GSlider extends GAbstractButton
{
    private static final int BODY_INDEX = 0;
    private static final int SLIDER_INDEX = 1;
    private static final int BORDER_INDEX = 2;
    private static final int SHAPE_ARRAY_SIZE = 3;
    
    private static final GShape DEFAULT_SHAPE = GNullShape.INSTANCE;

    private GShape bodyShape = DEFAULT_SHAPE;
    private GShape sliderShape = DEFAULT_SHAPE;
    private GShape borderShape = DEFAULT_SHAPE;
    
    private GAbstractButton slider;
    
    public GSlider()
    {
        setShape(new GShapeArray(SHAPE_ARRAY_SIZE));
        
        slider = new GAbstractButton();
        slider.setShape(sliderShape);
    }
    
    @Override
    public GShape getShape()
    {
        throw new IllegalAccessError();
    }
    
    @Override
    public void setShape(GShape shape)
    {
        throw new IllegalAccessError();
    }

    public GShape getBody()
    {
        return bodyShape;
    }

    public void setBody(GShape body)
    {
        this.bodyShape = body;
    }

    public GShape getSlider()
    {
        return sliderShape;
    }

    public void setSlider(GShape slider)
    {
        this.sliderShape = slider;
        this.slider.setShape(sliderShape);
    }

    public GShape getBorder()
    {
        return borderShape;
    }

    public void setBorder(GShape border)
    {
        this.borderShape = border;
    }
    
    public void enableBody(boolean enabled)
    {
        enable(bodyShape, BODY_INDEX, enabled);
    }
    
    public void enableSlider(boolean enabled)
    {
        enable(sliderShape, SLIDER_INDEX, enabled);
    }
    
    public void enableBorder(boolean enabled)
    {
        enable(borderShape, BORDER_INDEX, enabled);
    }
    
    private void enable(GShape shape, int index, boolean enabled)
    {
        GShapeArray array = (GShapeArray) getShape();
        if(enabled) array.setShape(shape, index);
        else array.setShape(null, index);
    }
}
