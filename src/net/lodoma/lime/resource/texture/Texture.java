package net.lodoma.lime.resource.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class Texture
{
    public static Texture NO_TEXTURE = new Texture(new int[] { 0xFFFFFFFF }, 1, 1, true);

    private int width;
    private int height;
    private int texture;
    
    public Texture(InputStream is) throws IOException
    {
        this(is, 0, 0, -1, -1);
    }
    
    public Texture(InputStream is, int offx, int offy, int width, int height) throws IOException
    {
        this.width = width;
        this.height = height;
        ByteBuffer bytes = loadTexture(is, offx, offy);
        texture = createTexture(bytes);
    }
    
    public Texture(int[] pixels, int width, int height, boolean hasAlpha)
    {
        this.width = width;
        this.height = height;
        ByteBuffer bytes = createByteBuffer(pixels, hasAlpha);
        texture = createTexture(bytes);
    }
    
    public void bind()
    {
        bind(0);
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
