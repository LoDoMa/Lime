package net.joritan.jlime.stage.singleplayer.world.gameobject.terrain;

import net.joritan.jlime.stage.singleplayer.world.Environment;
import net.joritan.jlime.stage.singleplayer.world.gameobject.GameObject;
import net.joritan.jlime.stage.singleplayer.world.gameobject.attribute.Bullet;
import net.joritan.jlime.stage.singleplayer.world.gameobject.segment.Segment;
import net.joritan.jlime.stage.singleplayer.world.gameobject.segment.SegmentType;
import net.joritan.jlime.stage.singleplayer.world.gameobject.segment.builder.SegmentBuilder;
import net.joritan.jlime.stage.singleplayer.world.gameobject.segment.builder.SegmentBuilderCircle;
import net.joritan.jlime.stage.singleplayer.world.gameobject.segment.builder.SegmentBuilderPolygon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Terrain implements GameObject
{
    private static final SegmentBuilder[] segmentBuilders;
    static
    {
        segmentBuilders = new SegmentBuilder[SegmentType.values().length];
        segmentBuilders[SegmentType.POLYGON.ordinal()] = new SegmentBuilderPolygon(false);
        segmentBuilders[SegmentType.CIRCLE.ordinal()] = new SegmentBuilderCircle(false);
    }

    protected final Environment environment;

    private Map<String, Segment> segments;

    public Terrain(Environment environment)
    {
        this.environment = environment;
        segments = new HashMap<String, Segment>();
    }

    protected void addSegment(String name, SegmentType type, Object... args)
    {
        segments.put(name, segmentBuilders[type.ordinal()].buildSegment(environment, args));
        segments.get(name).getBody().setUserData(this);
        if (this instanceof Bullet)
            segments.get(name).getBody().setBullet(true);
    }

    public Segment getSegment(String name)
    {
        return segments.get(name);
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
