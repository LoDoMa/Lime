package net.lodoma.lime.gui;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.util.Vector2;

public class TextField extends Button
{
    private class TextFieldButtonListener implements ButtonListener
    {
        @Override
        public void onClick(Button button, Vector2 mousePosition)
        {
            
        }
        
        @Override
        public void onHover(Button button, Vector2 mousePosition) {}
    }
    
    private class TextFieldButtonRenderer implements ButtonRenderer
    {
        @Override
        public void render(Button button)
        {
            
        }
    }
    
    private TextInput textInput;
    private boolean active;
    
    public TextField(Rectangle bounds, Text text)
    {
        super(bounds, null, null);
        setListener(new TextFieldButtonListener());
        setRenderer(new TextFieldButtonRenderer());
        textInput = new TextInput(text);
    }
    
    public String getText()
    {
        return textInput.getText().getText();
    }
    
    public boolean isActive()
    {
        return active;
    }
    
    @Override
    public void create()
    {
        super.create();
        textInput.create();
    }
    
    @Override
    public void destroy()
    {
        super.destroy();
        textInput.destroy();
    }
    
    @Override
    public void update(double timeDelta, Vector2 mousePosition)
    {
        super.update(timeDelta, mousePosition);
        
        if(isMouseClicked())
            active = true;
        else if(Input.getMouseUp(Input.MOUSE_BUTTON_LEFT))
            active = false;
        
        if(active)
            textInput.update(timeDelta, mousePosition);
    }
    
    @Override
    public void render()
    {
        super.render();
        
        Rectangle bounds = getBounds();
        glPushMatrix();
        glTranslatef(bounds.x, bounds.y, 0.0f);
        textInput.render();
        glPopMatrix();
    }
}
