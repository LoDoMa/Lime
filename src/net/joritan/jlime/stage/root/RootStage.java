package net.joritan.jlime.stage.root;

import net.joritan.jlime.stage.Stage;
import net.joritan.jlime.stage.StageManager;
import net.joritan.jlime.stage.editor.EditorStage;
import net.joritan.jlime.stage.singleplayer.SingleplayerStage;
import net.joritan.jlime.util.Input;
import net.joritan.jlime.util.RenderUtil;

import org.lwjgl.opengl.GL11;

public class RootStage extends Stage
{
    private BlueScreen bluescreen;
    
    public RootStage(StageManager manager)
    {
        super(null, null, manager);
    }
    
    public void setBluescreen(BlueScreen bluescreen)
    {
        this.bluescreen = bluescreen;
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
        GL11.glOrtho(0, 1, 0, 1, -1, 1);
        
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        
        GL11.glClearColor(0.5f, 0.0f, 0.0f, 1.0f);
    }
    
    @Override
    public void onDeselection()
    {
        
    }
    
    @Override
    public void update(float timeDelta)
    {
        if (Input.getKey(Input.KEY_F11))
            manager.pop();
        if (bluescreen != null)
        {
            return;
        }
        if (Input.getKey(Input.KEY_F1))
            manager.push(new SingleplayerStage(this));
        if (Input.getKey(Input.KEY_F2))
            new BlueScreen(manager, new String[]
            { "the stage manager", "loaded from the root stage (opt 2)", "the stage manager encountered a problem transfering execution", "", "problem detail: transfer to unknown stage" });
        if (Input.getKey(Input.KEY_F3))
            manager.push(new EditorStage(this));
    }
    
    @Override
    public void render()
    {
        if (bluescreen != null)
            bluescreen.render();
        else
        {
            RenderUtil.renderText("lime root stage 2.0", 0.1f, 0.90f, 0.015f, 0.02f);
            RenderUtil.renderText("as there is no load menu, the root stage is used", 0.1f, 0.85f, 0.015f, 0.02f);
            RenderUtil.renderText("for stage execution transfer (temporary)", 0.1f, 0.82f, 0.015f, 0.02f);
            RenderUtil.renderText("f1 - transfer execution to the singleplayer stage", 0.1f, 0.77f, 0.015f, 0.02f);
            RenderUtil.renderText("f2 - transfer execution to the multiplayer stage", 0.1f, 0.74f, 0.015f, 0.02f);
            RenderUtil.renderText("f3 - transfer execution to the editor stage", 0.1f, 0.71f, 0.015f, 0.02f);
            RenderUtil.renderText("f11 - terminate", 0.1f, 0.46f, 0.015f, 0.02f);
            RenderUtil.renderText("f12 - transfer execution to the root stage", 0.1f, 0.43f, 0.015f, 0.02f);
        }
    }
}
