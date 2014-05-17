package net.joritan.jlime.stage.singleplayer.world.gameobject.mask;

import net.joritan.jlime.util.Vector2;
import net.joritan.jlime.stage.singleplayer.world.gameobject.segment.Segment;

public class SegmentMaskBinding implements MaskBinding
{
    private Segment segment;
    private Vector2 staticPos;
    private Float staticAngle;

    public SegmentMaskBinding(Segment segment)
    {
        this.segment = segment;
        this.staticPos = null;
        this.staticAngle = null;
    }

    public void removeStaticPos()
    {
        staticPos = null;
    }

    public boolean hasStaticPos()
    {
        return staticPos != null;
    }

    public Vector2 getStaticPos()
    {
        return staticPos;
    }

    public void setStaticPos(Vector2 staticPos)
    {
        this.staticPos = staticPos;
    }

    public void removeStaticAngle()
    {
        staticAngle = null;
    }

    public boolean hasStaticAngle()
    {
        return staticAngle != null;
    }

    public float getStaticAngle()
    {
        return staticAngle;
    }

    public void setStaticAngle(float staticAngle)
    {
        this.staticAngle = staticAngle;
    }

    @Override
    public Vector2 getPosition()
    {
        return staticPos == null ? segment.getPosition() : staticPos;
    }

    @Override
    public float getRotation()
    {
        return staticAngle == null ? segment.getAngle() : staticAngle;
    }
}
