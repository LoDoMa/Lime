package net.lodoma.lime.texture;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;

public class Texture
{
    public static int NO_TEXTURE;
    
    public static void init()
    {
        NO_TEXTURE = glGenTextures();
        
        ByteBuffer buffer = BufferUtils.createByteBuffer(4);
        buffer.put((byte) 0xFF);
        buffer.put((byte) 0xFF);
        buffer.put((byte) 0xFF);
        buffer.put((byte) 0xFF);
        buffer.flip();
        
        glBindTexture(GL_TEXTURE_2D, NO_TEXTURE);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, 1, 1, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
    }
}
