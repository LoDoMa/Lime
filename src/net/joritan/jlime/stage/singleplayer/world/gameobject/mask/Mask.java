package net.joritan.jlime.stage.singleplayer.world.gameobject.mask;

import net.joritan.jlime.util.Texture;
import net.joritan.jlime.util.Vector2;
import net.joritan.jlime.stage.singleplayer.world.gameobject.GameObject;

import static org.lwjgl.opengl.GL11.*;

public class Mask implements GameObject
{
    private int vertexCount;

    private Texture texture;
    private Vector2[] vertices;
    private Vector2[] texcoords;

    private MaskBinding binding;

    private Mask(MaskBinding binding, Texture texture, Vector2[] vertices, boolean isStatic)
    {
        this.binding = binding;

        vertexCount = vertices.length;

        this.texture = texture;
        this.vertices = vertices;
    }

    public Mask(MaskBinding binding, Texture texture, Vector2[] vertices)
    {
        this(binding, texture, vertices, true);
        this.texcoords = new Vector2[vertexCount];
        for (int i = 0; i < vertexCount; i++)
            texcoords[i] = new Vector2(vertices[i].x, -(vertices[i].y));
    }

    public Mask(MaskBinding binding, Texture texture, Vector2[] vertices, Vector2[] texcoords)
    {
        this(binding, texture, vertices, false);
        this.texcoords = texcoords;
    }

    public MaskBinding getBinding()
    {
        return binding;
    }

    public void setBinding(MaskBinding binding)
    {
        this.binding = binding;
    }

    @Override
    public void update(float timeDelta)
    {

    }

    @Override
    public void render()
    {
        texture.bind();

        Vector2 translation = binding.getPosition();
        float angle = binding.getRotation();

        glTranslatef(translation.x, translation.y, 0.0f);
        glRotatef((float) Math.toDegrees(angle), 0.0f, 0.0f, 1.0f);

        glBegin(GL_POLYGON);
        for(int i = 0; i < vertexCount; i++)
        {
            glTexCoord2f(texcoords[i].x, texcoords[i].y);
            glVertex2f(vertices[i].x, vertices[i].y);
        }
        glEnd();

        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
