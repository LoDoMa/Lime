package net.joritan.jlime.stage.singleplayer.world;

import net.joritan.jlime.util.Input;
import net.joritan.jlime.util.Texture;
import net.joritan.jlime.util.Vector2;
import net.joritan.jlime.stage.singleplayer.world.gameobject.GameObject;
import net.joritan.jlime.stage.singleplayer.world.gameobject.entity.TE1;
import net.joritan.jlime.stage.singleplayer.world.gameobject.mask.StaticMaskBinding;
import net.joritan.jlime.stage.singleplayer.world.gameobject.terrain.Platform;
import net.joritan.jlime.stage.singleplayer.world.gameobject.mask.Mask;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.opengl.GL11.*;

public class Environment
{
    public static final int velocityIterations = 6;
    public static final int positionIterations = 2;

    public final World world;
    public Camera camera;

    private Set<GameObject> objects;

    public Environment()
    {
        world = new World(new Vec2(0, -16));
        world.setSleepingAllowed(true);

        camera = new Camera(-10, -10, 10, 10);

        objects = new HashSet<GameObject>();

        addGameObject(new TE1(this));

        int x1 = 0;
        int y1 = -10;
        int x2 = -5;
        int y2 = -10;
        for(int i = 0; i < 40; i++)
        {
            TEMP_addPlatform(x1, y1, x2, y2);
            x1 -= 5;
            x2 -= 5;
            y1 = y2;
            y2 += i;
        }
        x1 = 0;
        y1 = -10;
        x2 = 5;
        y2 = -10;
        for(int i = 0; i < 40; i++)
        {
            TEMP_addPlatform(x1, y1, x2, y2);
            x1 += 5;
            x2 += 5;
            y1 = y2;
            y2 += i;
        }
    }

    private void TEMP_addPlatform(float x1, float y1, float x2, float y2)
    {
        addGameObject(new Platform(this, x1, y1, x2, y2));
        addGameObject(new Mask(new StaticMaskBinding(new Vector2(0.0f, 0.0f), 0.0f), Texture.getTexture("dirt"),
            new Vector2[]
            {
                new Vector2(x1, y1),
                new Vector2(x2, y2),
                new Vector2(x2, y2 - 5.0f),
                new Vector2(x1, y1 - 5.0f)
            }));
    }

    public void addGameObject(GameObject object)
    {
        objects.add(object);
    }

    public void removeGameObject(GameObject object)
    {
        objects.remove(object);
    }

    public void update(float timeDelta)
    {
        for(GameObject object : objects)
            object.update(timeDelta);
        world.step(timeDelta, velocityIterations, positionIterations);
    }

    public void render()
    {
        camera.moveCamera();
        for (GameObject object : objects)
        {
            glPushMatrix();
            object.render();
            glPopMatrix();
        }
    }

    public void destroy()
    {

    }
}
