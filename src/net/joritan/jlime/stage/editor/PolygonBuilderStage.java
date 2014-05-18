package net.joritan.jlime.stage.editor;

import org.lwjgl.opengl.GL11;

import net.joritan.jlime.stage.Stage;
import net.joritan.jlime.stage.StageLoader;

public class PolygonBuilderStage extends Stage
{

    public PolygonBuilderStage(Stage parentStage, Object... manager)
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
    public void update(float timeDelta)
    {
        
    }

    @Override
    public void render()
    {
        
    }
}
