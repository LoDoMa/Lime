package net.joritan.jlime.stage.singleplayer.world.gameobject.segment.builder;

import net.joritan.jlime.stage.singleplayer.world.Environment;
import net.joritan.jlime.stage.singleplayer.world.gameobject.segment.Segment;

public interface SegmentBuilder
{
    public Segment buildSegment(Environment environment, Object... args);
}
