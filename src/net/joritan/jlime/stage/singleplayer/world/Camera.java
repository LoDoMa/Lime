package net.joritan.jlime.stage.singleplayer.world;

import net.joritan.jlime.util.Vector2;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class Camera
{
    private Vector2 camNW;
    private Vector2 camSE;
    private Rectangle.Float cameraRect;

    public Camera(float nwX, float nwY, float seX, float seY)
    {
        camNW = new Vector2(nwX, nwY);
        camSE = new Vector2(seX, seY);
    }

    public void moveCamera()
    {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(camNW.x, camSE.x, camNW.y, camSE.y, -1, 1);
        cameraRect = new Rectangle.Float(camNW.x, camNW.y, camSE.x - camNW.x, camSE.y - camNW.y);

        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public boolean onScreen(float nwX, float nwY, float seX, float seY)
    {
        return cameraRect.intersects(nwX, nwY, seX - nwX, seY - nwY);
    }
}
