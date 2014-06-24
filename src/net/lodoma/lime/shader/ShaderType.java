package net.lodoma.lime.shader;

import org.lwjgl.opengl.GL20;

public enum ShaderType
{
    VERTEX(GL20.GL_VERTEX_SHADER),
    FRAGMENT(GL20.GL_FRAGMENT_SHADER);
    
    public final int GLENUM;
    
    private ShaderType(int glenum)
    {
        GLENUM = glenum;
    }
}
