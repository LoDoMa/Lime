package net.joritan.jlime.stage.singleplayer;

import net.joritan.jlime.stage.StageLoader;

import static org.lwjgl.opengl.GL11.glClearColor;

public class SingleplayerLoader extends StageLoader
{
    @Override
    public void load()
    {

    }

    @Override
    public void render()
    {
        glClearColor(0.0f, 0.0f, 1.0f, 0.0f);
    }
}
