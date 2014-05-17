package net.joritan.jlime.stage.singleplayer.world.gameobject.mask;

import net.joritan.jlime.util.Vector2;

public class StaticMaskBinding implements MaskBinding
{
    private Vector2 pos;
    private float angle;

    public StaticMaskBinding(Vector2 pos, float angle)
    {
        this.pos = pos;
        this.angle = angle;
    }

    @Override
    public Vector2 getPosition()
    {
        return pos;
    }

    @Override
    public float getRotation()
    {
        return angle;
    }
}
