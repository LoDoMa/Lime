package net.joritan.jlime.stage.singleplayer.world.gameobject.segment.builder;

import net.joritan.jlime.stage.singleplayer.world.Environment;
import net.joritan.jlime.stage.singleplayer.world.gameobject.segment.Segment;
import net.joritan.jlime.stage.singleplayer.world.gameobject.segment.SegmentCircle;

public class SegmentBuilderCircle implements SegmentBuilder
{
    private boolean isDynamic;

    public SegmentBuilderCircle(boolean isDynamic)
    {
        this.isDynamic = isDynamic;
    }

    @Override
    public Segment buildSegment(Environment environment, Object... args)
    {
        return new SegmentCircle(environment, isDynamic, args);
    }
}
