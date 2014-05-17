package net.joritan.jlime.stage.singleplayer.world.gameobject.segment.builder;

import net.joritan.jlime.stage.singleplayer.world.Environment;
import net.joritan.jlime.stage.singleplayer.world.gameobject.segment.Segment;
import net.joritan.jlime.stage.singleplayer.world.gameobject.segment.SegmentPolygon;

public class SegmentBuilderPolygon implements SegmentBuilder
{
    private boolean isDynamic;

    public SegmentBuilderPolygon(boolean isDynamic)
    {
        this.isDynamic = isDynamic;
    }

    @Override
    public Segment buildSegment(Environment environment, Object... args)
    {
        return new SegmentPolygon(environment, isDynamic, args);
    }
}
