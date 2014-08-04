package net.lodoma.lime.shader.light;

public interface Light
{
    public int getTypeHash();
    public void useProgram();
    public void render();
}
