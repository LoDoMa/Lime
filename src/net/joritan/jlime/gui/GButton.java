package net.joritan.jlime.gui;

public class GButton extends GUIElement
{
    private GShape shape;
    private GShapeDecoration decoration;
    
    public GButton(GShape shape, GShapeDecoration decoration, GUIEnvironment environment)
    {
        super(environment);
        this.shape = shape;
    }
    
    @Override
    public void update(float timeDelta)
    {
        
    }
    
    @Override
    public void render()
    {
        
    }
}
