package net.lodoma.lime.texture.animation;

public interface Animation
{
    public void start();
    public void update(float time);
    public void render();
    public void delete();
}
