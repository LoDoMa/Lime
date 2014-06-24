package net.lodoma.lime.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class Texture
{
    public final int id;
    
    public Texture(String fileName)
    {
        id = GL11.glGenTextures();
        load(fileName);
    }
    
    public void bind(int unitOffset)
    {
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + unitOffset);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
    }
    
    void delete()
    {
        GL11.glDeleteTextures(id);
    }
    
    private void load(String fileName)
    {
        try
        {
            BufferedImage image = ImageIO.read(new File(fileName));
            int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());

            ByteBuffer buffer = BufferUtils.createByteBuffer(image.getHeight() * image.getWidth() * 4);
            boolean hasAlpha = image.getColorModel().hasAlpha();

            for(int y = 0; y < image.getHeight(); y++)
                for(int x = 0; x < image.getWidth(); x++)
                {
                    int pixel = pixels[y * image.getWidth() + x];

                    buffer.put((byte)((pixel >> 16) & 0xFF));
                    buffer.put((byte)((pixel >> 8) & 0xFF));
                    buffer.put((byte)((pixel) & 0xFF));
                    if(hasAlpha)
                        buffer.put((byte)((pixel >> 24) & 0xFF));
                    else
                        buffer.put((byte)(0xFF));
                }

            buffer.flip();

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);

            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, image.getWidth(), image.getHeight(),
                              0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
