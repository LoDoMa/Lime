package net.joritan.jlime.stage.editor;

import org.lwjgl.opengl.GL11;

import net.joritan.jlime.stage.Stage;
import net.joritan.jlime.util.Input;
import net.joritan.jlime.util.RenderUtil;

public class EditorChoiceStage extends Stage
{
    private int selectedIndex;
    private String[] choiceTexts;
    private long choiceDescriptor;
    private EditorChoiceCallback callback;
    
    public EditorChoiceStage(Stage parentStage, String[] choiceTexts, long choiceDescriptor, EditorChoiceCallback callback)
    {
        super(parentStage, null);
        this.selectedIndex = 0;
        this.choiceTexts = choiceTexts;
        this.choiceDescriptor = choiceDescriptor;
        this.callback = callback;
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
    public void update(float timeDelta)
    {
        if(Input.getKeyDown(Input.KEY_TAB))
            selectedIndex = (selectedIndex + 1) % choiceTexts.length;
        if(Input.getKeyDown(Input.KEY_RETURN))
        {
            EditorChoiceResult result = new EditorChoiceResult(selectedIndex, choiceDescriptor);
            callback.choiceCallback(result);
            manager.pop();
        }
    }

    @Override
    public void render()
    {
        float y = 0.9f;
        for(int i = 0; i < choiceTexts.length; i++)
        {
            if(selectedIndex == i)
                RenderUtil.renderTextBox(choiceTexts[i], 0.3f, y, 0.4f, 0.03f, 0.015f, 0.02f, 1.0f, 1.0f, 1.0f, 0.3f, 0.3f, 0.3f);
            else
                RenderUtil.renderTextBox(choiceTexts[i], 0.3f, y, 0.4f, 0.03f, 0.015f, 0.02f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f);
            y -= 0.04f;
        }
    }
}
