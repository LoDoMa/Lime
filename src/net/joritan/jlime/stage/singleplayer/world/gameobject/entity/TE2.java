package net.joritan.jlime.stage.singleplayer.world.gameobject.entity;

import net.joritan.jlime.util.Input;
import net.joritan.jlime.util.Vector2;
import net.joritan.jlime.stage.singleplayer.world.Camera;
import net.joritan.jlime.stage.singleplayer.world.Environment;
import net.joritan.jlime.stage.singleplayer.world.gameobject.segment.Segment;
import net.joritan.jlime.stage.singleplayer.world.gameobject.segment.SegmentType;

public class TE2 extends Entity
{
    public TE2(Environment environment)
    {
        super(environment);

        addSegment("body", SegmentType.POLYGON, new Vector2[]
                {
                        new Vector2(0, 0),
                        new Vector2(0, 1),
                        new Vector2(1, 1),
                        new Vector2(1, 0)
                }, 0.09f, 0.3f, 0.0f);
        getSegment("body").setLinearDamping(0.3f);
        getSegment("body").setTransform(new Vector2(0.0f, 0.0f), 0.0f);
        getSegment("body").setFixedRotation(true);
    }

    @Override
    public void update(float timeDelta)
    {
        super.update(timeDelta);

        Segment body = getSegment("body");

        float maxVelX = 20.0f;
        if(Input.getKey(Input.KEY_LEFT)) body.setLinearVelocity(body.getLinearVelocity().sub(new Vector2(10.0f * timeDelta, 0.0f)));
        if(Input.getKey(Input.KEY_RIGHT)) body.setLinearVelocity(body.getLinearVelocity().add(new Vector2(10.0f * timeDelta, 0.0f)));
        if(Input.getKeyDown(Input.KEY_UP)) body.setLinearVelocity(body.getLinearVelocity().add(new Vector2(0.0f, 10.0f)));
        body.setLinearVelocity(body.getLinearVelocity().containX(-maxVelX, maxVelX));

        Vector2 bodyPosition = body.getPosition();
        Vector2 cornerUpLeft = bodyPosition.add(new Vector2(-10.0f, -10.0f));
        Vector2 cornerDownRight = bodyPosition.add(new Vector2(10.0f, 10.0f));

        Camera camera = new Camera(cornerUpLeft.x, cornerUpLeft.y,
                cornerDownRight.x, cornerDownRight.y);
        environment.camera = camera;
    }
}
