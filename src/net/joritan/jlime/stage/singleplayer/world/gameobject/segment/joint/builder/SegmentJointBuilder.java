package net.joritan.jlime.stage.singleplayer.world.gameobject.segment.joint.builder;

import net.joritan.jlime.stage.singleplayer.world.Environment;
import net.joritan.jlime.stage.singleplayer.world.gameobject.segment.Segment;
import net.joritan.jlime.stage.singleplayer.world.gameobject.segment.joint.SegmentJoint;

public interface SegmentJointBuilder
{
    public SegmentJoint buildSegmentJoint(Environment environment, Segment segmentA, Segment segmentB, Object... args);
}
