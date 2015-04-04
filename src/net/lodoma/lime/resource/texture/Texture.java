package net.lodoma.lime.resource.texture;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import net.lodoma.lime.Lime;
import net.lodoma.lime.util.OsHelper;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class Texture
{
    public static Texture NO_TEXTURE = new Texture(new int[] { 0xFFFFFFFF }, 1, 1, true);

    private static Object lock = new Object();
    private static List<Texture> createList = new ArrayList<Texture>();
    private static List<Texture> destroyList = new ArrayList<Texture>();
    private static Map<String, Texture> loadedTextures = new HashMap<String, Texture>();
    
    public static void referenceUp(String name)
    {
        synchronized (lock)
        {
            Texture texture = loadedTextures.get(name);
            if (texture == null)
            {
                texture = new Texture();
                texture.name = name;
                loadedTextures.put(name, texture);
                createList.add(texture);
            }
            texture.refc++;
        }
    }
    
    public static void referenceDown(String name)
    {
        synchronized (lock)
        {
            Texture texture = loadedTextures.get(name);
            if (texture == null)
                throw new NullPointerException();
            texture.refc--;
            if (texture.refc == 0)
            {
                loadedTextures.remove(texture.name);
                destroyList.add(texture);
            }
        }
    }
    
    /**
     * Call this method only in renderer thread!
     */
    public static void updateAll()
    {
        synchronized (lock)
        {
            for (Texture texture : createList)
                try
                {
                    texture.width = -1;
                    texture.height = -1;
                    String path = OsHelper.JARPATH + "res/textures/" + texture.name + ".png";
                    ByteBuffer bytes = texture.loadTexture(new FileInputStream(path), 0, 0);
                    texture.texture = texture.createTexture(bytes);
                    Lime.LOGGER.D("Loaded texture " + texture.name);
                }
                catch (IOException e)
                {
                    Lime.LOGGER.C("Failed to load texture " + texture.name);
                    Lime.LOGGER.log(e);
                    Lime.forceExit(e);
                }

            for (Texture texture : destroyList)
            {
                texture.delete();
                Lime.LOGGER.D("Deleted texture " + texture.name);
            }
            
            createList.clear();
            destroyList.clear();
        }
    }
    
    public static void forceDeleteAll()
    {
        synchronized (lock)
        {
            destroyList.addAll(loadedTextures.values());
            destroyList.clear();
        }
    }
    
    public static Texture get(String name)
    {
        synchronized (lock)
        {
            Texture texture = loadedTextures.get(name);
            if (texture == null)
                throw new NullPointerException();
            return texture;
        }
    }

    private String name;
    private int refc = 0;
    
    private int width;
    private int height;
    private int texture;
    
    private Texture()
    {
        
    }
    
    private Texture(InputStream is) throws IOException
    {
        this(is, 0, 0, -1, -1);
    }
    
    private Texture(InputStream is, int offx, int offy, int width, int height) throws IOException
    {
        this.width = width;
        this.height = height;
        ByteBuffer bytes = loadTexture(is, offx, offy);
        texture = createTexture(bytes);
    }
    
    private Texture(int[] pixels, int width, int height, boolean hasAlpha)
    {
        this.width = width;
        this.height = height;
        ByteBuffer bytes = createByteBuffer(pixels, hasAlpha);
        texture = createTexture(bytes);
    }
    
    public void bind(int slot)
    {
        glActiveTexture(GL_TEXTURE0 + slot);
        glBindTexture(GL_TEXTURE_2D, texture);
    }
    
    public void delete()
    {
        glDeleteTextures(texture);
    }
    
    private ByteBuffer loadTexture(InputStream is, int offx, int offy) throws IOException
    {
        BufferedImage image = ImageIO.read(is);
        if (width == -1) width = image.getWidth();
        if (height == -1) height = image.getHeight();
        int[] pixels = image.getRGB(offx, offy, width, height, null, 0, width);
        boolean hasAlpha = image.getColorModel().hasAlpha();
        
        return createByteBuffer(pixels, hasAlpha);
    }
    
    private ByteBuffer createByteBuffer(int[] pixels, boolean hasAlpha)
    {
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
        
        for(int y = 0; y < height; y++)
            for(int x = 0; x < width; x++)
            {
                int pixel = pixels[y * width + x];
                
                buffer.put((byte)((pixel >> 16) & 0xFF));
                buffer.put((byte)((pixel >> 8) & 0xFF));
                buffer.put((byte)((pixel) & 0xFF));
                if(hasAlpha)
                    buffer.put((byte)((pixel >> 24) & 0xFF));
                else
                    buffer.put((byte)(0xFF));
            }
        
        buffer.flip();
        return buffer;
    }
    
    private int createTexture(ByteBuffer bytes)
    {
        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, bytes);
        
        return texture;
    }
}
