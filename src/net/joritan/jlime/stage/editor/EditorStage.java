package net.joritan.jlime.stage.editor;

import org.lwjgl.opengl.GL11;

import net.joritan.jlime.stage.Stage;
import net.joritan.jlime.util.Input;
import net.joritan.jlime.util.RenderUtil;

public class EditorStage extends Stage implements EditorChoiceCallback
{
    private int selectedIndex;
    
    public EditorStage(Stage parentStage, Object... manager)
    {
        super(parentStage, null, manager);
    }
    
    @Override
    public void onCreation()
    {
        
    }
    
    @Override
    public void onDestruction()
    {
        
    }
    
    @Override
    public void onSelection()
    {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0f, 1.0f, 0.0f, 1.0f, -1.0f, 1.0f);
        
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }
    
    @Override
    public void onDeselection()
    {
        
    }
    
    @Override
    public void choiceCallback(EditorChoiceResult result)
    {
        if (result.DESCRIPTOR == 0)
        {
            switch(result.CHOICE)
            {
            case 0:
                manager.push(new PolygonBuilderStage(this));
                break;
            }
        }
    }
    
    @Override
    public void update(float timeDelta)
    {
        if (Input.getKeyDown(Input.KEY_TAB))
            selectedIndex = (selectedIndex + 1) % 2;
        if (Input.getKeyDown(Input.KEY_RETURN))
            switch (selectedIndex)
            {
            case 0:
                manager.push(new EditorChoiceStage(this, new String[]
                { "polygon", "circle" }, 0, this));
                break;
            }
    }
    
    @Override
    public void render()
    {
        if (selectedIndex == 0)
            RenderUtil.renderButton("add shape", 0.05f, 0.95f, 0.2f, 0.03f, 0.015f, 0.02f, 1.0f, 1.0f, 1.0f, 0.7f, 0.7f, 0.7f);
        else
            RenderUtil.renderButton("add shape", 0.05f, 0.95f, 0.2f, 0.03f, 0.015f, 0.02f, 1.0f, 1.0f, 1.0f, 0.3f, 0.3f, 0.3f);
        if (selectedIndex == 1)
            RenderUtil.renderButton("add joint", 0.05f, 0.91f, 0.2f, 0.03f, 0.015f, 0.02f, 1.0f, 1.0f, 1.0f, 0.7f, 0.7f, 0.7f);
        else
            RenderUtil.renderButton("add joint", 0.05f, 0.91f, 0.2f, 0.03f, 0.015f, 0.02f, 1.0f, 1.0f, 1.0f, 0.3f, 0.3f, 0.3f);
    }
}
