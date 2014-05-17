package net.joritan.jlime.stage.singleplayer.world.gameobject.entity;

import net.joritan.jlime.stage.singleplayer.world.Environment;
import net.joritan.jlime.stage.singleplayer.world.gameobject.GameObject;
import net.joritan.jlime.stage.singleplayer.world.gameobject.attribute.Bullet;
import net.joritan.jlime.stage.singleplayer.world.gameobject.segment.Segment;
import net.joritan.jlime.stage.singleplayer.world.gameobject.segment.SegmentType;
import net.joritan.jlime.stage.singleplayer.world.gameobject.segment.builder.SegmentBuilder;
import net.joritan.jlime.stage.singleplayer.world.gameobject.segment.builder.SegmentBuilderCircle;
import net.joritan.jlime.stage.singleplayer.world.gameobject.segment.builder.SegmentBuilderPolygon;
import net.joritan.jlime.stage.singleplayer.world.gameobject.segment.joint.SegmentJointType;
import net.joritan.jlime.stage.singleplayer.world.gameobject.segment.joint.SegmentJoint;
import net.joritan.jlime.stage.singleplayer.world.gameobject.segment.joint.builder.SegmentJointBuilder;
import net.joritan.jlime.stage.singleplayer.world.gameobject.segment.joint.builder.SegmentJointBuilderRevolute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Entity implements GameObject
{
    private static final SegmentBuilder[] segmentBuilders;
    static
    {
        segmentBuilders = new SegmentBuilder[SegmentType.values().length];
        segmentBuilders[SegmentType.POLYGON.ordinal()] = new SegmentBuilderPolygon(true);
        segmentBuilders[SegmentType.CIRCLE.ordinal()] = new SegmentBuilderCircle(true);
    }

    private static final SegmentJointBuilder[] segmentJointBuilders;
    static
    {
        segmentJointBuilders = new SegmentJointBuilder[SegmentJointType.values().length];
        segmentJointBuilders[SegmentJointType.REVOLUTE.ordinal()] = new SegmentJointBuilderRevolute();
    }

    protected final Environment environment;

    private Map<String, Segment> segments;
    private Map<String, SegmentJoint> joints;

    public Entity(Environment environment)
    {
        this.environment = environment;
        segments = new HashMap<String, Segment>();
        joints = new HashMap<String, SegmentJoint>();
    }

    protected void addSegment(String name, SegmentType type, Object... args)
    {
        segments.put(name, segmentBuilders[type.ordinal()].buildSegment(environment, args));
        segments.get(name).getBody().setUserData(this);
        if (this instanceof Bullet)
            segments.get(name).getBody().setBullet(true);
    }

    protected void addSegmentJoint(String name, String aName, String bName, SegmentJointType type, Object... args)
    {
        joints.put(name, segmentJointBuilders[type.ordinal()].buildSegmentJoint(environment,
                segments.get(aName), segments.get(bName), args));
    }

    public Segment getSegment(String name)
    {
        return segments.get(name);
    }

    public SegmentJoint getSegmentJoint(String name)
    {
        return joints.get(name);
    }

    @Override
    public void update(float timeDelta)
    {
        List<Segment> segmentList = new ArrayList<Segment>(segments.values());
        for(Segment segment : segmentList)
            segment.update(timeDelta);
    }

    @Override
    public void render()
    {
        List<Segment> segmentList = new ArrayList<Segment>(segments.values());
        for(Segment segment : segmentList)
            segment.render();
    }
}
