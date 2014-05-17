package net.joritan.jlime.util;

import net.joritan.jlime.stage.root.BlueScreen;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

public class Texture
{
    private static final Map<String, Texture> loadedTextures;
    static
    {
        loadedTextures = new HashMap<String, Texture>();
    }

    public static void addTexture(String name, Texture texture)
    {
        loadedTextures.put(name, texture);
    }

    public static Texture getTexture(String name)
    {
        return loadedTextures.get(name);
    }

    public static void removeTexture(String name)
    {
        loadedTextures.remove(name);
    }

    private int id;
    private int x;
    private int y;
    private int w;
    private int h;

    public Texture(String filename)
    {
        x = y = 0;
        w = h = -1;
        loadTexture(filename);
    }

    public Texture(String filename, int x, int y, int w, int h)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        loadTexture(filename);
    }

    public void bind()
    {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void unload()
    {
        glDeleteBuffers(id);
    }

    private void loadTexture(String fileName)
    {
        try
        {
            BufferedImage image = ImageIO.read(new File(fileName));
            if(w == -1) w = image.getWidth();
            if(h == -1) h = image.getHeight();
            int[] pixels = image.getRGB(x, y, w, h, null, 0, w);

            ByteBuffer buffer = BufferUtils.createByteBuffer(w * h * 4);
            boolean hasAlpha = image.getColorModel().hasAlpha();

            for(int y = 0; y < h; y++)
            {
                for(int x = 0; x < w; x++)
                {
                    int pixel = pixels[y * w + x];

                    buffer.put((byte)((pixel >> 16) & 0xFF));
                    buffer.put((byte)((pixel >> 8) & 0xFF));
                    buffer.put((byte)((pixel) & 0xFF));
                    if(hasAlpha)
                        buffer.put((byte)((pixel >> 24) & 0xFF));
                    else
                        buffer.put((byte)(0xFF));
                }
            }

            buffer.flip();

            id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, id);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            glBindTexture(GL_TEXTURE_2D, 0);
        }
        catch(Exception e)
        {
            new BlueScreen(null, e, new String[] {"Texture failed to load"});
        }
    }
}
