package net.joritan.jlime.stage.root;

import net.joritan.jlime.stage.editor.EditorStage;
import net.joritan.jlime.stage.singleplayer.SingleplayerStage;
import net.joritan.jlime.util.Input;
import net.joritan.jlime.stage.Stage;
import net.joritan.jlime.stage.StageManager;
import net.joritan.jlime.util.RenderUtil;
import net.joritan.jlime.util.Texture;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class RootStage extends Stage
{
    private static final float VISIBLE_DURATION = 1.0f;
    private float currentDuration = 0.0f;

    private boolean firstRun = true;
    private boolean runSpecial = false;

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
        String font = "abcdefghijklmnopqrstuvwxyz0123456789.!?/*+-$=%\"'#&_ (),:\\|{}<>[]";
        for(int i = 0; i < font.length(); i++)
            Texture.addTexture("$letter" + font.charAt(i), new Texture("res/font.png", (i % 13) * 8, (i / 13) * 8, 8, 8));
    }

    @Override
    public void onDestruction()
    {

    }

    @Override
    public void onSelection()
    {

    }

    @Override
    public void onDeselection()
    {

    }

    @Override
    public void update(float timeDelta)
    {
        if(bluescreen != null)
        {
            return;
        }
        currentDuration += timeDelta;
        if (Input.getKeyDown(Input.KEY_F3))
            runSpecial = true;
        if (currentDuration > VISIBLE_DURATION)
        {
            if (!firstRun)
                manager.pop();
            else
            {
                firstRun = false;
                if (runSpecial) manager.push(new EditorStage(this));
                else manager.push(new SingleplayerStage(this));
            }
        }
    }

    @Override
    public void render()
    {
        if(bluescreen != null) bluescreen.render();
        else
        {
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            glOrtho(0, 1, 0, 1, -1, 1);

            glMatrixMode(GL_MODELVIEW);
            glEnable(GL_TEXTURE_2D);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glEnable(GL_BLEND);

            GL11.glClearColor(0.5f, 0.0f, 0.0f, 1.0f);

            RenderUtil.renderText("press f3 to special boot", 0.04f, 0.04f);
        }
    }
}
